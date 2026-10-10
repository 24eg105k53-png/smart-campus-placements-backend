package com.smartcampus.placement.service.impl;

import com.smartcampus.placement.dto.request.ApplicationStatusUpdateRequest;
import com.smartcampus.placement.dto.response.ApplicationResponse;
import com.smartcampus.placement.entity.*;
import com.smartcampus.placement.exception.AccessDeniedException;
import com.smartcampus.placement.exception.DuplicateResourceException;
import com.smartcampus.placement.exception.ResourceNotFoundException;
import com.smartcampus.placement.repository.ApplicationRepository;
import com.smartcampus.placement.repository.JobPostingRepository;
import com.smartcampus.placement.repository.StudentProfileRepository;
import com.smartcampus.placement.repository.UserRepository;
import com.smartcampus.placement.service.ApplicationService;
import com.smartcampus.placement.service.EligibilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;
    private final EligibilityService eligibilityService;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  StudentProfileRepository studentProfileRepository,
                                  JobPostingRepository jobPostingRepository,
                                  UserRepository userRepository,
                                  EligibilityService eligibilityService) {
        this.applicationRepository = applicationRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.userRepository = userRepository;
        this.eligibilityService = eligibilityService;
    }

    @Override
    @Transactional
    public ApplicationResponse applyForJob(Long jobId, Long studentUserId) {
        StudentProfile student = studentProfileRepository.findByUserId(studentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + studentUserId));

        JobPosting job = jobPostingRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job opening not found with ID: " + jobId));

        if (applicationRepository.existsByStudentProfileIdAndJobPostingId(student.getId(), job.getId())) {
            throw new DuplicateResourceException("You have already submitted an application for this job posting.");
        }

        // Authoritative eligibility check before saving application
        eligibilityService.validateEligibilityOrThrow(student, job);

        Application application = new Application(student, job);
        application = applicationRepository.save(application);

        return mapToResponse(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications(Long studentUserId) {
        StudentProfile student = studentProfileRepository.findByUserId(studentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + studentUserId));

        List<Application> applications = applicationRepository.findByStudentProfileId(student.getId());
        return applications.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicantsByJob(Long jobId, Long userId) {
        JobPosting job = jobPostingRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job opening not found with ID: " + jobId));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // If recruiter, check company ownership
        if (user.getRole() == User.Role.RECRUITER) {
            if (job.getCompany().getRecruiterUser() == null ||
                !job.getCompany().getRecruiterUser().getId().equals(userId)) {
                throw new AccessDeniedException("Unauthorized: You can only view applicants for your company's job postings.");
            }
        }

        List<Application> applications = applicationRepository.findByJobPostingId(jobId);
        return applications.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatusUpdateRequest request, Long userId) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + applicationId));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (user.getRole() == User.Role.RECRUITER) {
            Company company = application.getJobPosting().getCompany();
            if (company.getRecruiterUser() == null || !company.getRecruiterUser().getId().equals(userId)) {
                throw new AccessDeniedException("Unauthorized: You can only review candidates for your company's postings.");
            }
        }

        application.setStatus(request.getStatus());
        if (request.getRecruiterNotes() != null) {
            application.setRecruiterNotes(request.getRecruiterNotes());
        }
        application.setUpdatedAt(Instant.now());

        application = applicationRepository.save(application);
        return mapToResponse(application);
    }

    private ApplicationResponse mapToResponse(Application app) {
        ApplicationResponse resp = new ApplicationResponse();
        resp.setId(app.getId());

        StudentProfile student = app.getStudentProfile();
        resp.setStudentProfileId(student.getId());
        resp.setStudentName(student.getUser().getFullName());
        resp.setStudentEmail(student.getUser().getEmail());
        resp.setStudentRegistrationNumber(student.getStudentRegistrationNumber());
        resp.setStudentDepartment(student.getDepartment());
        resp.setStudentCgpa(student.getCgpa());
        resp.setStudentBacklogs(student.getActiveBacklogs());
        resp.setStudentResumeFilePath(student.getResumeFilePath());

        List<String> skills = student.getStudentSkills().stream()
            .map(ss -> ss.getSkill().getName())
            .sorted()
            .collect(Collectors.toList());
        resp.setStudentSkills(skills);

        JobPosting job = app.getJobPosting();
        resp.setJobId(job.getId());
        resp.setJobTitle(job.getTitle());
        resp.setCompanyName(job.getCompany().getName());
        resp.setCtcRange(job.getCtcRange());

        resp.setStatus(app.getStatus().name());
        resp.setRecruiterNotes(app.getRecruiterNotes());
        resp.setAppliedAt(app.getAppliedAt());
        resp.setUpdatedAt(app.getUpdatedAt());

        // Calculate skill overlap match score
        long matchingSkills = job.getRequiredSkills().stream()
            .filter(req -> skills.stream().anyMatch(s -> s.equalsIgnoreCase(req.getName())))
            .count();
        double totalReq = !job.getRequiredSkills().isEmpty() ? job.getRequiredSkills().size() : 1.0;
        resp.setMatchScore(Math.round((matchingSkills / totalReq) * 100.0 * 10.0) / 10.0);

        return resp;
    }
}
