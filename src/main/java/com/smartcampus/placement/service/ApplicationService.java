package com.smartcampus.placement.service;

import com.smartcampus.placement.dto.request.ApplicationStatusUpdateRequest;
import com.smartcampus.placement.dto.response.ApplicationResponse;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse applyForJob(Long jobId, Long studentUserId);
    List<ApplicationResponse> getMyApplications(Long studentUserId);
    List<ApplicationResponse> getApplicantsByJob(Long jobId, Long recruiterOrTpoUserId);
    ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatusUpdateRequest request, Long recruiterOrTpoUserId);
}
