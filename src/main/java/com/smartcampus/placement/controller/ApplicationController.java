package com.smartcampus.placement.controller;

import com.smartcampus.placement.dto.request.ApplicationStatusUpdateRequest;
import com.smartcampus.placement.dto.response.ApplicationResponse;
import com.smartcampus.placement.service.ApplicationService;
import com.smartcampus.placement.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/apply/{jobId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApplicationResponse> applyForJob(@PathVariable Long jobId) {
        Long userId = SecurityUtils.getCurrentUserId();
        ApplicationResponse response = applicationService.applyForJob(jobId, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(applicationService.getMyApplications(userId));
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'TPO')")
    public ResponseEntity<List<ApplicationResponse>> getJobApplicants(@PathVariable Long jobId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(applicationService.getApplicantsByJob(jobId, userId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RECRUITER', 'TPO')")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(applicationService.updateApplicationStatus(id, request, userId));
    }
}
