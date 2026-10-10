package com.smartcampus.placement.service;

import com.smartcampus.placement.dto.request.JobPostingCreateRequest;
import com.smartcampus.placement.dto.request.JobPostingUpdateRequest;
import com.smartcampus.placement.dto.response.JobPostingResponse;
import com.smartcampus.placement.dto.response.SkillGapResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobPostingService {
    Page<JobPostingResponse> getActiveJobs(String search, Pageable pageable, Long currentUserId);
    JobPostingResponse getJobById(Long id, Long currentUserId);
    JobPostingResponse createJob(JobPostingCreateRequest request, Long currentUserId);
    JobPostingResponse updateJob(Long id, JobPostingUpdateRequest request, Long currentUserId);
    List<JobPostingResponse> getJobsByCompany(Long companyId);
    SkillGapResponse calculateSkillGap(Long jobId, Long studentUserId);
}
