package com.smartcampus.placement.service.impl;

import com.smartcampus.placement.dto.request.JobPostingCreateRequest;
import com.smartcampus.placement.dto.request.JobPostingUpdateRequest;
import com.smartcampus.placement.dto.response.EligibilityResultResponse;
import com.smartcampus.placement.dto.response.JobPostingResponse;
import com.smartcampus.placement.dto.response.SkillGapResponse;
import com.smartcampus.placement.entity.*;
import com.smartcampus.placement.exception.BadRequestException;
import com.smartcampus.placement.exception.ResourceNotFoundException;
import com.smartcampus.placement.integration.ai.AiServiceClient;
import com.smartcampus.placement.integration.ai.dto.AiSkillGapResponse;
import com.smartcampus.placement.repository.*;
import com.smartcampus.placement.service.EligibilityService;
import com.smartcampus.placement.service.JobPostingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final ApplicationRepository applicationRepository;
    private final EligibilityService eligibilityService;
    private final AiServiceClient aiServiceClient;

    public JobPostingServiceImpl(JobPostingRepository jobPostingRepository,
                                 CompanyRepository companyRepository,
                                 SkillRepository skillRepository,
                                 StudentProfileRepository studentProfileRepository,
                                 ApplicationRepository applicationRepository,
                                 EligibilityService eligibilityService,
                                 AiServiceClient aiServiceClient) {
        this.jobPostingRepository = jobPostingRepository;
        this.companyRepository = companyRepository;
        this.skillRepository = skillRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.applicationRepository = applicationRepository;
        this.eligibilityService = eligibilityService;
        this.aiServiceClient = aiServiceClient;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobPostingResponse> getActiveJobs(String search, Pageable pageable, Long currentUserId) {
        Page<JobPosting> jobs = jobPostingRepository.searchActiveJobs(search, pageable);
        Optional<StudentProfile> studentProfile = currentUserId != null
            ? studentProfileRepository.findByUserId(currentUserId)
            : Optional.empty();

        List<JobPostingResponse> responses = jobs.getContent().stream()
            .map(job -> mapToResponse(job, studentProfile.orElse(null)))
            .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, jobs.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public JobPostingResponse getJobById(Long id, Long currentUserId) {
        JobPosting job = jobPostingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Job opening not found with ID: " + id));

        Optional<StudentProfile> studentProfile = currentUserId != null
            ? studentProfileRepository.findByUserId(currentUserId)
            : Optional.empty();

        return mapToResponse(job, studentProfile.orElse(null));
    }

    @Override
    @Transactional
    public JobPostingResponse createJob(JobPostingCreateRequest request, Long currentUserId) {
        Company company;
        if (request.getCompanyId() != null) {
            company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.getCompanyId()));
        } else {
            company = companyRepository.findByRecruiterUserId(currentUserId)
                .orElseThrow(() -> new BadRequestException("No company associated with recruiter account"));
        }

        JobPosting job = new JobPosting();
        job.setCompany(company);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setJobLocation(request.getJobLocation());
        job.setMinimumCgpa(request.getMinimumCgpa());
        job.setMaximumBacklogs(request.getMaximumBacklogs());
        job.setEligibleDepartments(request.getEligibleDepartments());
        job.setTargetGraduationYear(request.getTargetGraduationYear());
        job.setCtcRange(request.getCtcRange());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setStatus(JobPosting.JobStatus.PUBLISHED);

        if (request.getRequiredSkills() != null) {
            Set<Skill> skills = new HashSet<>();
            for (String skillName : request.getRequiredSkills()) {
                if (skillName == null || skillName.trim().isEmpty()) continue;
                String normalized = skillName.trim().toLowerCase();
                Skill skill = skillRepository.findByNormalizedName(normalized)
                    .orElseGet(() -> skillRepository.save(new Skill(skillName.trim(), normalized, "TECHNICAL")));
                skills.add(skill);
            }
            job.setRequiredSkills(skills);
        }

        job = jobPostingRepository.save(job);
        return mapToResponse(job, null);
    }

    @Override
    @Transactional
    public JobPostingResponse updateJob(Long id, JobPostingUpdateRequest request, Long currentUserId) {
        JobPosting job = jobPostingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Job opening not found with ID: " + id));

        if (job.getCompany().getRecruiterUser() != null &&
            !job.getCompany().getRecruiterUser().getId().equals(currentUserId)) {
            // Note: TPO bypass logic is handled via method security or caller
        }

        if (request.getTitle() != null) job.setTitle(request.getTitle());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getJobLocation() != null) job.setJobLocation(request.getJobLocation());
        if (request.getMinimumCgpa() != null) job.setMinimumCgpa(request.getMinimumCgpa());
        if (request.getMaximumBacklogs() != null) job.setMaximumBacklogs(request.getMaximumBacklogs());
        if (request.getEligibleDepartments() != null) job.setEligibleDepartments(request.getEligibleDepartments());
        if (request.getTargetGraduationYear() != null) job.setTargetGraduationYear(request.getTargetGraduationYear());
        if (request.getCtcRange() != null) job.setCtcRange(request.getCtcRange());
        if (request.getApplicationDeadline() != null) job.setApplicationDeadline(request.getApplicationDeadline());
        if (request.getStatus() != null) job.setStatus(request.getStatus());

        if (request.getRequiredSkills() != null) {
            Set<Skill> skills = new HashSet<>();
            for (String skillName : request.getRequiredSkills()) {
                if (skillName == null || skillName.trim().isEmpty()) continue;
                String normalized = skillName.trim().toLowerCase();
                Skill skill = skillRepository.findByNormalizedName(normalized)
                    .orElseGet(() -> skillRepository.save(new Skill(skillName.trim(), normalized, "TECHNICAL")));
                skills.add(skill);
            }
            job.setRequiredSkills(skills);
        }

        job = jobPostingRepository.save(job);
        return mapToResponse(job, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobPostingResponse> getJobsByCompany(Long companyId) {
        List<JobPosting> jobs = jobPostingRepository.findByCompanyId(companyId);
        return jobs.stream().map(j -> mapToResponse(j, null)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SkillGapResponse calculateSkillGap(Long jobId, Long studentUserId) {
        JobPosting job = jobPostingRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job opening not found with ID: " + jobId));
        StudentProfile student = studentProfileRepository.findByUserId(studentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + studentUserId));

        List<String> studentSkills = student.getStudentSkills().stream()
            .map(ss -> ss.getSkill().getName())
            .collect(Collectors.toList());

        List<String> requiredSkills = job.getRequiredSkills().stream()
            .map(Skill::getName)
            .collect(Collectors.toList());

        AiSkillGapResponse aiResp = aiServiceClient.analyzeSkillGap(
            studentSkills,
            requiredSkills,
            job.getDescription()
        );

        SkillGapResponse resp = new SkillGapResponse();
        resp.setMatchPercentage(aiResp.getMatchPercentage());
        resp.setMatchingSkills(aiResp.getMatchingSkills());
        resp.setMissingSkills(aiResp.getMissingSkills());
        resp.setExtraSkills(aiResp.getExtraSkills());
        resp.setCosineSimilarity(aiResp.getCosineSimilarity());
        resp.setExplanation(aiResp.getExplanation());
        resp.setAiPowered(true);
        return resp;
    }

    private JobPostingResponse mapToResponse(JobPosting job, StudentProfile currentStudent) {
        JobPostingResponse resp = new JobPostingResponse();
        resp.setId(job.getId());
        resp.setCompanyId(job.getCompany().getId());
        resp.setCompanyName(job.getCompany().getName());
        resp.setCompanyIndustry(job.getCompany().getIndustry());
        resp.setTitle(job.getTitle());
        resp.setDescription(job.getDescription());
        resp.setJobLocation(job.getJobLocation());
        resp.setMinimumCgpa(job.getMinimumCgpa());
        resp.setMaximumBacklogs(job.getMaximumBacklogs());
        resp.setEligibleDepartments(job.getEligibleDepartments());
        resp.setTargetGraduationYear(job.getTargetGraduationYear());
        resp.setCtcRange(job.getCtcRange());
        resp.setApplicationDeadline(job.getApplicationDeadline());
        resp.setStatus(job.getStatus().name());

        List<String> reqSkills = job.getRequiredSkills().stream()
            .map(Skill::getName)
            .sorted()
            .collect(Collectors.toList());
        resp.setRequiredSkills(reqSkills);

        long applicantsCount = applicationRepository.findByJobPostingId(job.getId()).size();
        resp.setApplicantsCount(applicantsCount);

        if (currentStudent != null) {
            EligibilityResultResponse eligibility = eligibilityService.checkEligibility(currentStudent, job);
            resp.setIsEligibleForCurrentUser(eligibility.isEligible());
            boolean applied = applicationRepository.existsByStudentProfileIdAndJobPostingId(currentStudent.getId(), job.getId());
            resp.setHasApplied(applied);
        }

        return resp;
    }
}
