package com.smartcampus.placement.controller;

import com.smartcampus.placement.dto.request.StudentProfileUpdateRequest;
import com.smartcampus.placement.dto.response.StudentProfileResponse;
import com.smartcampus.placement.entity.ResumeAnalysis;
import com.smartcampus.placement.integration.ai.dto.AiReadinessResponse;
import com.smartcampus.placement.service.ResumeService;
import com.smartcampus.placement.service.StudentProfileService;
import com.smartcampus.placement.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentProfileService studentProfileService;
    private final ResumeService resumeService;

    public StudentController(StudentProfileService studentProfileService, ResumeService resumeService) {
        this.studentProfileService = studentProfileService;
        this.resumeService = resumeService;
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentProfileResponse> getProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(studentProfileService.getStudentProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<StudentProfileResponse> updateProfile(@Valid @RequestBody StudentProfileUpdateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(studentProfileService.updateStudentProfile(userId, request));
    }

    @GetMapping("/skills")
    public ResponseEntity<List<StudentProfileResponse.SkillItem>> getSkills() {
        Long userId = SecurityUtils.getCurrentUserId();
        StudentProfileResponse profile = studentProfileService.getStudentProfile(userId);
        return ResponseEntity.ok(profile.getSkills());
    }

    @PostMapping("/skills")
    public ResponseEntity<List<StudentProfileResponse.SkillItem>> addSkills(@RequestBody List<String> skillNames) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(studentProfileService.addSkillsToProfile(userId, skillNames));
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> removeSkill(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        studentProfileService.removeSkillFromProfile(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resume/upload")
    public ResponseEntity<ResumeAnalysis> uploadResume(@RequestParam("file") MultipartFile file) {
        Long userId = SecurityUtils.getCurrentUserId();
        ResumeAnalysis analysis = resumeService.uploadAndParseResume(userId, file);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/resume/analysis")
    public ResponseEntity<ResumeAnalysis> getResumeAnalysis() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(resumeService.getLatestResumeAnalysis(userId));
    }

    @GetMapping("/readiness")
    public ResponseEntity<AiReadinessResponse> getReadiness() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(studentProfileService.getStudentReadiness(userId));
    }
}
