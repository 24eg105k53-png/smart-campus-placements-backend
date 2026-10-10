package com.smartcampus.placement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "resume_analyses")
public class ResumeAnalysis extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false, unique = true)
    private StudentProfile studentProfile;

    @Column(name = "file_reference", nullable = false, length = 255)
    private String fileReference;

    @Column(name = "file_type", nullable = false, length = 10)
    private String fileType; // PDF, DOCX

    @Column(name = "processing_status", nullable = false, length = 30)
    private String processingStatus = "COMPLETED";

    @Column(name = "extracted_skills_json", columnDefinition = "TEXT")
    private String extractedSkillsJson; // JSON array string e.g. ["Java","Spring Boot"]

    @Column(name = "extracted_summary", columnDefinition = "TEXT")
    private String extractedSummary;

    @Column(name = "extracted_cgpa", precision = 4, scale = 2)
    private BigDecimal extractedCgpa;

    @Column(name = "raw_text_length")
    private Integer rawTextLength;

    public ResumeAnalysis() {
    }

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }

    public String getFileReference() {
        return fileReference;
    }

    public void setFileReference(String fileReference) {
        this.fileReference = fileReference;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
    }

    public String getExtractedSkillsJson() {
        return extractedSkillsJson;
    }

    public void setExtractedSkillsJson(String extractedSkillsJson) {
        this.extractedSkillsJson = extractedSkillsJson;
    }

    public String getExtractedSummary() {
        return extractedSummary;
    }

    public void setExtractedSummary(String extractedSummary) {
        this.extractedSummary = extractedSummary;
    }

    public BigDecimal getExtractedCgpa() {
        return extractedCgpa;
    }

    public void setExtractedCgpa(BigDecimal extractedCgpa) {
        this.extractedCgpa = extractedCgpa;
    }

    public Integer getRawTextLength() {
        return rawTextLength;
    }

    public void setRawTextLength(Integer rawTextLength) {
        this.rawTextLength = rawTextLength;
    }
}
