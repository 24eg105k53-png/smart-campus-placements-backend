package com.smartcampus.placement.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampus.placement.entity.*;
import com.smartcampus.placement.exception.ResourceNotFoundException;
import com.smartcampus.placement.integration.ai.AiServiceClient;
import com.smartcampus.placement.integration.ai.dto.AiResumeParseResponse;
import com.smartcampus.placement.repository.ResumeAnalysisRepository;
import com.smartcampus.placement.repository.SkillRepository;
import com.smartcampus.placement.repository.StudentProfileRepository;
import com.smartcampus.placement.service.ResumeService;
import com.smartcampus.placement.util.FileStorageUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final StudentProfileRepository studentProfileRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final SkillRepository skillRepository;
    private final FileStorageUtil fileStorageUtil;
    private final AiServiceClient aiServiceClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeServiceImpl(StudentProfileRepository studentProfileRepository,
                             ResumeAnalysisRepository resumeAnalysisRepository,
                             SkillRepository skillRepository,
                             FileStorageUtil fileStorageUtil,
                             AiServiceClient aiServiceClient) {
        this.studentProfileRepository = studentProfileRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.skillRepository = skillRepository;
        this.fileStorageUtil = fileStorageUtil;
        this.aiServiceClient = aiServiceClient;
    }

    @Override
    @Transactional
    public ResumeAnalysis uploadAndParseResume(Long studentUserId, MultipartFile file) {
        StudentProfile student = studentProfileRepository.findByUserId(studentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + studentUserId));

        String savedFilename = fileStorageUtil.storeFile(file);
        student.setResumeFilePath(savedFilename);

        // Call AI microservice
        AiResumeParseResponse aiResponse = aiServiceClient.parseResume(file);

        ResumeAnalysis analysis = resumeAnalysisRepository.findByStudentProfileId(student.getId())
            .orElseGet(() -> {
                ResumeAnalysis ra = new ResumeAnalysis();
                ra.setStudentProfile(student);
                return ra;
            });

        analysis.setFileReference(savedFilename);
        analysis.setFileType(aiResponse.getFileType() != null ? aiResponse.getFileType() : "PDF");
        analysis.setProcessingStatus(aiResponse.getStatus() != null ? aiResponse.getStatus() : "COMPLETED");
        analysis.setExtractedSummary(aiResponse.getExtractedSummary());
        analysis.setExtractedCgpa(aiResponse.getExtractedCgpa());
        analysis.setRawTextLength(1200);

        try {
            analysis.setExtractedSkillsJson(objectMapper.writeValueAsString(aiResponse.getExtractedSkills()));
        } catch (JsonProcessingException e) {
            analysis.setExtractedSkillsJson("[]");
        }

        // Add extracted skills to student profile
        if (aiResponse.getExtractedSkills() != null) {
            for (String skillName : aiResponse.getExtractedSkills()) {
                if (skillName == null || skillName.trim().isEmpty()) continue;
                String normalized = skillName.trim().toLowerCase();
                Skill skill = skillRepository.findByNormalizedName(normalized)
                    .orElseGet(() -> skillRepository.save(new Skill(skillName.trim(), normalized, "TECHNICAL")));

                boolean alreadyHasSkill = student.getStudentSkills().stream()
                    .anyMatch(ss -> ss.getSkill().getId().equals(skill.getId()));

                if (!alreadyHasSkill) {
                    student.getStudentSkills().add(new StudentSkill(student, skill, "INTERMEDIATE"));
                }
            }
        }

        studentProfileRepository.save(student);
        return resumeAnalysisRepository.save(analysis);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeAnalysis getLatestResumeAnalysis(Long studentUserId) {
        StudentProfile student = studentProfileRepository.findByUserId(studentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + studentUserId));

        return resumeAnalysisRepository.findByStudentProfileId(student.getId())
            .orElseThrow(() -> new ResourceNotFoundException("No resume analysis found. Please upload a resume first."));
    }

    @Override
    @Transactional(readOnly = true)
    public Resource loadResumeAsResource(Long studentUserId) {
        StudentProfile student = studentProfileRepository.findByUserId(studentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + studentUserId));

        if (student.getResumeFilePath() == null) {
            throw new ResourceNotFoundException("Student has not uploaded a resume yet.");
        }

        try {
            Path filePath = fileStorageUtil.loadFileAsPath(student.getResumeFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + student.getResumeFilePath());
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found: " + student.getResumeFilePath());
        }
    }
}
