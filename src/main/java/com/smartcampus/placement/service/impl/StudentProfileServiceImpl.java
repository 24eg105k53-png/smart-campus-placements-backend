package com.smartcampus.placement.service.impl;

import com.smartcampus.placement.dto.request.StudentProfileUpdateRequest;
import com.smartcampus.placement.dto.response.StudentProfileResponse;
import com.smartcampus.placement.entity.*;
import com.smartcampus.placement.exception.ResourceNotFoundException;
import com.smartcampus.placement.integration.ai.AiServiceClient;
import com.smartcampus.placement.integration.ai.dto.AiReadinessResponse;
import com.smartcampus.placement.repository.*;
import com.smartcampus.placement.service.StudentProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final ApplicationRepository applicationRepository;
    private final AiServiceClient aiServiceClient;

    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository,
                                     SkillRepository skillRepository,
                                     StudentSkillRepository studentSkillRepository,
                                     ApplicationRepository applicationRepository,
                                     AiServiceClient aiServiceClient) {
        this.studentProfileRepository = studentProfileRepository;
        this.skillRepository = skillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.applicationRepository = applicationRepository;
        this.aiServiceClient = aiServiceClient;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentProfileResponse getStudentProfile(Long userId) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + userId));

        return mapToResponse(profile);
    }

    @Override
    @Transactional
    public StudentProfileResponse updateStudentProfile(Long userId, StudentProfileUpdateRequest request) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + userId));

        if (request.getPhoneNumber() != null) profile.setPhoneNumber(request.getPhoneNumber());
        if (request.getGithubProfileUrl() != null) profile.setGithubProfileUrl(request.getGithubProfileUrl());
        if (request.getLinkedinProfileUrl() != null) profile.setLinkedinProfileUrl(request.getLinkedinProfileUrl());
        if (request.getCgpa() != null) profile.setCgpa(request.getCgpa());
        if (request.getActiveBacklogs() != null) profile.setActiveBacklogs(request.getActiveBacklogs());

        if (request.getSkills() != null) {
            syncSkills(profile, request.getSkills());
        }

        profile = studentProfileRepository.save(profile);
        return mapToResponse(profile);
    }

    @Override
    @Transactional
    public List<StudentProfileResponse.SkillItem> addSkillsToProfile(Long userId, List<String> skillNames) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + userId));

        for (String skillName : skillNames) {
            if (skillName == null || skillName.trim().isEmpty()) continue;
            String normalized = skillName.trim().toLowerCase();
            Skill skill = skillRepository.findByNormalizedName(normalized)
                .orElseGet(() -> skillRepository.save(new Skill(skillName.trim(), normalized, "TECHNICAL")));

            boolean exists = profile.getStudentSkills().stream()
                .anyMatch(ss -> ss.getSkill().getId().equals(skill.getId()));

            if (!exists) {
                StudentSkill studentSkill = new StudentSkill(profile, skill, "INTERMEDIATE");
                profile.getStudentSkills().add(studentSkill);
            }
        }

        studentProfileRepository.save(profile);
        return profile.getStudentSkills().stream()
            .map(ss -> new StudentProfileResponse.SkillItem(ss.getSkill().getId(), ss.getSkill().getName(), ss.getProficiencyLevel()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeSkillFromProfile(Long userId, Long skillId) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + userId));

        profile.getStudentSkills().removeIf(ss -> ss.getSkill().getId().equals(skillId));
        studentProfileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public AiReadinessResponse getStudentReadiness(Long userId) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + userId));

        int appliedCount = applicationRepository.findByStudentProfileId(profile.getId()).size();
        boolean hasResume = profile.getResumeFilePath() != null && !profile.getResumeFilePath().isEmpty();
        int skillsCount = profile.getStudentSkills().size();

        return aiServiceClient.evaluateReadiness(
            profile.getCgpa(),
            profile.getActiveBacklogs(),
            skillsCount,
            hasResume,
            appliedCount
        );
    }

    private void syncSkills(StudentProfile profile, List<String> skillNames) {
        profile.getStudentSkills().clear();
        for (String skillName : skillNames) {
            if (skillName == null || skillName.trim().isEmpty()) continue;
            String normalized = skillName.trim().toLowerCase();
            Skill skill = skillRepository.findByNormalizedName(normalized)
                .orElseGet(() -> skillRepository.save(new Skill(skillName.trim(), normalized, "TECHNICAL")));
            profile.getStudentSkills().add(new StudentSkill(profile, skill, "INTERMEDIATE"));
        }
    }

    private StudentProfileResponse mapToResponse(StudentProfile profile) {
        StudentProfileResponse resp = new StudentProfileResponse();
        resp.setId(profile.getId());
        resp.setUserId(profile.getUser().getId());
        resp.setFullName(profile.getUser().getFullName());
        resp.setEmail(profile.getUser().getEmail());
        resp.setStudentRegistrationNumber(profile.getStudentRegistrationNumber());
        resp.setUniversity(profile.getUniversity());
        resp.setDegree(profile.getDegree());
        resp.setDepartment(profile.getDepartment());
        resp.setGraduationYear(profile.getGraduationYear());
        resp.setCgpa(profile.getCgpa());
        resp.setActiveBacklogs(profile.getActiveBacklogs());
        resp.setResumeFilePath(profile.getResumeFilePath());
        resp.setPhoneNumber(profile.getPhoneNumber());
        resp.setGithubProfileUrl(profile.getGithubProfileUrl());
        resp.setLinkedinProfileUrl(profile.getLinkedinProfileUrl());

        List<StudentProfileResponse.SkillItem> skills = profile.getStudentSkills().stream()
            .map(ss -> new StudentProfileResponse.SkillItem(ss.getSkill().getId(), ss.getSkill().getName(), ss.getProficiencyLevel()))
            .sorted(Comparator.comparing(StudentProfileResponse.SkillItem::getName))
            .collect(Collectors.toList());
        resp.setSkills(skills);

        if (profile.getResumeAnalysis() != null) {
            ResumeAnalysis ra = profile.getResumeAnalysis();
            List<String> extractedSkills = Collections.emptyList();
            if (ra.getExtractedSkillsJson() != null && !ra.getExtractedSkillsJson().isEmpty()) {
                extractedSkills = Arrays.stream(ra.getExtractedSkillsJson().replace("[", "").replace("]", "").replace("\"", "").split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            }
            resp.setResumeSummary(new StudentProfileResponse.ResumeSummary(
                ra.getProcessingStatus(),
                extractedSkills,
                ra.getExtractedSummary(),
                ra.getExtractedCgpa()
            ));
        }

        return resp;
    }
}
