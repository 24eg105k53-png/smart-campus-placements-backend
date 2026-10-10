package com.smartcampus.placement.service;

import com.smartcampus.placement.dto.response.EligibilityResultResponse;
import com.smartcampus.placement.entity.JobPosting;
import com.smartcampus.placement.entity.StudentProfile;

public interface EligibilityService {
    EligibilityResultResponse checkEligibility(StudentProfile student, JobPosting job);
    EligibilityResultResponse checkEligibility(Long studentProfileId, Long jobId);
    void validateEligibilityOrThrow(StudentProfile student, JobPosting job);
}
