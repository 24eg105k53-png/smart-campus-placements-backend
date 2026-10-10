package com.smartcampus.placement.integration.ai.dto;

import java.math.BigDecimal;
import java.util.List;

public class AiResumeParseResponse {

    private String status;
    private String fileName;
    private String fileType;
    private List<String> extractedSkills;
    private String extractedSummary;
    private BigDecimal extractedCgpa;
    private Double confidenceScore;
    private List<String> detectedSections;

    public AiResumeParseResponse() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public List<String> getExtractedSkills() { return extractedSkills; }
    public void setExtractedSkills(List<String> extractedSkills) { this.extractedSkills = extractedSkills; }
    public String getExtractedSummary() { return extractedSummary; }
    public void setExtractedSummary(String extractedSummary) { this.extractedSummary = extractedSummary; }
    public BigDecimal getExtractedCgpa() { return extractedCgpa; }
    public void setExtractedCgpa(BigDecimal extractedCgpa) { this.extractedCgpa = extractedCgpa; }
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public List<String> getDetectedSections() { return detectedSections; }
    public void setDetectedSections(List<String> detectedSections) { this.detectedSections = detectedSections; }
}
