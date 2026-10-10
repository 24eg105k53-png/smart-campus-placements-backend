package com.smartcampus.placement.controller;

import com.smartcampus.placement.dto.request.JobPostingCreateRequest;
import com.smartcampus.placement.dto.request.JobPostingUpdateRequest;
import com.smartcampus.placement.dto.response.EligibilityResultResponse;
import com.smartcampus.placement.dto.response.JobPostingResponse;
import com.smartcampus.placement.dto.response.SkillGapResponse;
import com.smartcampus.placement.entity.StudentProfile;
import com.smartcampus.placement.repository.StudentProfileRepository;
import com.smartcampus.placement.service.EligibilityService;
import com.smartcampus.placement.service.JobPostingService;
import com.smartcampus.placement.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final EligibilityService eligibilityService;
    private final StudentProfileRepository studentProfileRepository;

    public JobPostingController(JobPostingService jobPostingService,
                                EligibilityService eligibilityService,
                                StudentProfileRepository studentProfileRepository) {
        this.jobPostingService = jobPostingService;
        this.eligibilityService = eligibilityService;
        this.studentProfileRepository = studentProfileRepository;
    }

    @GetMapping
    public ResponseEntity<Page<JobPostingResponse>> getAllJobs(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Long currentUserId = null;
        try {
            currentUserId = SecurityUtils.getCurrentUserId();
        } catch (Exception ignored) {}

        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        return ResponseEntity.ok(jobPostingService.getActiveJobs(search, pageable, currentUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostingResponse> getJobById(@PathVariable Long id) {
        Long currentUserId = null;
        try {
            currentUserId = SecurityUtils.getCurrentUserId();
        } catch (Exception ignored) {}

        return ResponseEntity.ok(jobPostingService.getJobById(id, currentUserId));
    }

    @GetMapping("/{id}/eligibility")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EligibilityResultResponse> checkJobEligibility(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        StudentProfile student = studentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Student profile not found"));

        return ResponseEntity.ok(eligibilityService.checkEligibility(student.getId(), id));
    }

    @GetMapping("/{id}/skill-gap")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<SkillGapResponse> getSkillGap(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(jobPostingService.calculateSkillGap(id, userId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECRUITER', 'TPO')")
    public ResponseEntity<JobPostingResponse> createJob(@Valid @RequestBody JobPostingCreateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        JobPostingResponse response = jobPostingService.createJob(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'TPO')")
    public ResponseEntity<JobPostingResponse> updateJob(@PathVariable Long id, @Valid @RequestBody JobPostingUpdateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(jobPostingService.updateJob(id, request, userId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobPostingResponse>> getCompanyJobs(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobPostingService.getJobsByCompany(companyId));
    }
}
