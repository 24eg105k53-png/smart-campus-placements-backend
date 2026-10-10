package com.smartcampus.placement.service;

import com.smartcampus.placement.dto.response.EligibilityResultResponse;
import com.smartcampus.placement.entity.JobPosting;
import com.smartcampus.placement.entity.StudentProfile;
import com.smartcampus.placement.entity.User;
import com.smartcampus.placement.exception.IneligibleApplicationException;
import com.smartcampus.placement.repository.JobPostingRepository;
import com.smartcampus.placement.repository.StudentProfileRepository;
import com.smartcampus.placement.service.impl.EligibilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EligibilityServiceTest {

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private JobPostingRepository jobPostingRepository;

    private EligibilityServiceImpl eligibilityService;

    @BeforeEach
    void setUp() {
        eligibilityService = new EligibilityServiceImpl(studentProfileRepository, jobPostingRepository);
    }

    @Test
    @DisplayName("Student meeting all criteria is marked eligible")
    void shouldBeEligibleWhenAllCriteriaMet() {
        User user = new User("Test Student", "test@campus.edu", "pass", User.Role.STUDENT);
        StudentProfile student = new StudentProfile(user, "REG001", "Computer Science and Engineering", 2026, new BigDecimal("8.50"), 0);

        JobPosting job = new JobPosting();
        job.setMinimumCgpa(new BigDecimal("7.50"));
        job.setMaximumBacklogs(0);
        job.setEligibleDepartments("Computer Science and Engineering,Information Technology");
        job.setTargetGraduationYear(2026);

        EligibilityResultResponse result = eligibilityService.checkEligibility(student, job);

        assertTrue(result.isEligible());
        assertTrue(result.isCgpaEligible());
        assertTrue(result.isBacklogsEligible());
        assertTrue(result.isDepartmentEligible());
        assertTrue(result.isGraduationYearEligible());
        assertTrue(result.getFailureReasons().isEmpty());
    }

    @Test
    @DisplayName("Student with CGPA below cutoff is marked ineligible with reason")
    void shouldBeIneligibleWhenCgpaBelowCutoff() {
        User user = new User("Test Student", "test@campus.edu", "pass", User.Role.STUDENT);
        StudentProfile student = new StudentProfile(user, "REG001", "Computer Science and Engineering", 2026, new BigDecimal("7.49"), 0);

        JobPosting job = new JobPosting();
        job.setMinimumCgpa(new BigDecimal("7.50"));
        job.setMaximumBacklogs(0);
        job.setEligibleDepartments("Computer Science and Engineering");
        job.setTargetGraduationYear(2026);

        EligibilityResultResponse result = eligibilityService.checkEligibility(student, job);

        assertFalse(result.isEligible());
        assertFalse(result.isCgpaEligible());
        assertEquals(1, result.getFailureReasons().size());
        assertTrue(result.getFailureReasons().get(0).contains("Current CGPA"));
    }

    @Test
    @DisplayName("Student with active backlogs exceeding cutoff is marked ineligible")
    void shouldBeIneligibleWhenBacklogsExceed() {
        User user = new User("Test Student", "test@campus.edu", "pass", User.Role.STUDENT);
        StudentProfile student = new StudentProfile(user, "REG001", "Computer Science and Engineering", 2026, new BigDecimal("8.00"), 1);

        JobPosting job = new JobPosting();
        job.setMinimumCgpa(new BigDecimal("7.00"));
        job.setMaximumBacklogs(0);
        job.setEligibleDepartments("Computer Science and Engineering");
        job.setTargetGraduationYear(2026);

        EligibilityResultResponse result = eligibilityService.checkEligibility(student, job);

        assertFalse(result.isEligible());
        assertFalse(result.isBacklogsEligible());
        assertTrue(result.getFailureReasons().get(0).contains("Active backlogs count"));
    }

    @Test
    @DisplayName("validateEligibilityOrThrow throws IneligibleApplicationException")
    void shouldThrowWhenIneligible() {
        User user = new User("Test Student", "test@campus.edu", "pass", User.Role.STUDENT);
        StudentProfile student = new StudentProfile(user, "REG001", "Civil Engineering", 2026, new BigDecimal("8.00"), 0);

        JobPosting job = new JobPosting();
        job.setMinimumCgpa(new BigDecimal("7.00"));
        job.setMaximumBacklogs(0);
        job.setEligibleDepartments("Computer Science and Engineering,Information Technology");
        job.setTargetGraduationYear(2026);

        assertThrows(IneligibleApplicationException.class, () ->
            eligibilityService.validateEligibilityOrThrow(student, job)
        );
    }
}
