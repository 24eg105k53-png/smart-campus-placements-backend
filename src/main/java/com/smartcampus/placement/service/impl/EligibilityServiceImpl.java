package com.smartcampus.placement.service.impl;

import com.smartcampus.placement.dto.response.EligibilityResultResponse;
import com.smartcampus.placement.entity.JobPosting;
import com.smartcampus.placement.entity.StudentProfile;
import com.smartcampus.placement.exception.IneligibleApplicationException;
import com.smartcampus.placement.exception.ResourceNotFoundException;
import com.smartcampus.placement.repository.JobPostingRepository;
import com.smartcampus.placement.repository.StudentProfileRepository;
import com.smartcampus.placement.service.EligibilityService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EligibilityServiceImpl implements EligibilityService {

    private final StudentProfileRepository studentProfileRepository;
    private final JobPostingRepository jobPostingRepository;

    public EligibilityServiceImpl(StudentProfileRepository studentProfileRepository,
                                  JobPostingRepository jobPostingRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    @Override
    public EligibilityResultResponse checkEligibility(StudentProfile student, JobPosting job) {
        EligibilityResultResponse result = new EligibilityResultResponse();
        List<String> failureReasons = new ArrayList<>();

        result.setStudentCgpa(student.getCgpa());
        result.setMinimumCgpa(job.getMinimumCgpa());
        boolean cgpaEligible = student.getCgpa().compareTo(job.getMinimumCgpa()) >= 0;
        result.setCgpaEligible(cgpaEligible);
        if (!cgpaEligible) {
            failureReasons.add(String.format("Current CGPA (%.2f) does not meet the minimum requirement of %.2f",
                student.getCgpa().doubleValue(), job.getMinimumCgpa().doubleValue()));
        }

        result.setStudentBacklogs(student.getActiveBacklogs());
        result.setMaximumBacklogs(job.getMaximumBacklogs());
        boolean backlogsEligible = student.getActiveBacklogs() <= job.getMaximumBacklogs();
        result.setBacklogsEligible(backlogsEligible);
        if (!backlogsEligible) {
            failureReasons.add(String.format("Active backlogs count (%d) exceeds the maximum allowed (%d)",
                student.getActiveBacklogs(), job.getMaximumBacklogs()));
        }

        result.setStudentDepartment(student.getDepartment());
        result.setEligibleDepartments(job.getEligibleDepartments());
        boolean departmentEligible = isDepartmentEligible(student.getDepartment(), job.getEligibleDepartments());
        result.setDepartmentEligible(departmentEligible);
        if (!departmentEligible) {
            failureReasons.add(String.format("Student branch '%s' is not in the eligible department list [%s]",
                student.getDepartment(), job.getEligibleDepartments()));
        }

        result.setStudentGraduationYear(student.getGraduationYear());
        result.setTargetGraduationYear(job.getTargetGraduationYear());
        boolean gradYearEligible = student.getGraduationYear().equals(job.getTargetGraduationYear());
        result.setGraduationYearEligible(gradYearEligible);
        if (!gradYearEligible) {
            failureReasons.add(String.format("Graduation year (%d) does not match target batch of %d",
                student.getGraduationYear(), job.getTargetGraduationYear()));
        }

        boolean overallEligible = cgpaEligible && backlogsEligible && departmentEligible && gradYearEligible;
        result.setEligible(overallEligible);
        result.setFailureReasons(failureReasons);

        return result;
    }

    @Override
    public EligibilityResultResponse checkEligibility(Long studentProfileId, Long jobId) {
        StudentProfile student = studentProfileRepository.findById(studentProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found with ID: " + studentProfileId));
        JobPosting job = jobPostingRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with ID: " + jobId));

        return checkEligibility(student, job);
    }

    @Override
    public void validateEligibilityOrThrow(StudentProfile student, JobPosting job) {
        EligibilityResultResponse result = checkEligibility(student, job);
        if (!result.isEligible()) {
            throw new IneligibleApplicationException(
                "You do not meet the mandatory eligibility criteria for this job posting.",
                result.getFailureReasons()
            );
        }
    }

    private boolean isDepartmentEligible(String studentDept, String eligibleDepts) {
        if (eligibleDepts == null || eligibleDepts.trim().isEmpty() || eligibleDepts.equalsIgnoreCase("ALL")) {
            return true;
        }
        if (studentDept == null) {
            return false;
        }

        String normalizedStudentDept = studentDept.toLowerCase().trim();
        return Arrays.stream(eligibleDepts.split(","))
            .map(String::trim)
            .map(String::toLowerCase)
            .anyMatch(d -> d.equals(normalizedStudentDept) ||
                           normalizedStudentDept.contains(d) ||
                           d.contains(normalizedStudentDept));
    }
}
