package com.smartcampus.placement.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class StudentProfileResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String studentRegistrationNumber;
    private String university;
    private String degree;
    private String department;
    private Integer graduationYear;
    private BigDecimal cgpa;
    private Integer activeBacklogs;
    private String resumeFilePath;
    private String phoneNumber;
    private String githubProfileUrl;
    private String linkedinProfileUrl;
    private List<SkillItem> skills;
    private ResumeSummary resumeSummary;

    public static class SkillItem {
        private Long id;
        private String name;
        private String proficiencyLevel;

        public SkillItem() {}
        public SkillItem(Long id, String name, String proficiencyLevel) {
            this.id = id;
            this.name = name;
            this.proficiencyLevel = proficiencyLevel;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getProficiencyLevel() { return proficiencyLevel; }
        public void setProficiencyLevel(String proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }
    }

    public static class ResumeSummary {
        private String processingStatus;
        private List<String> extractedSkills;
        private String extractedSummary;
        private BigDecimal extractedCgpa;

        public ResumeSummary() {}
        public ResumeSummary(String processingStatus, List<String> extractedSkills, String extractedSummary, BigDecimal extractedCgpa) {
            this.processingStatus = processingStatus;
            this.extractedSkills = extractedSkills;
            this.extractedSummary = extractedSummary;
            this.extractedCgpa = extractedCgpa;
        }

        public String getProcessingStatus() { return processingStatus; }
        public void setProcessingStatus(String processingStatus) { this.processingStatus = processingStatus; }
        public List<String> getExtractedSkills() { return extractedSkills; }
        public void setExtractedSkills(List<String> extractedSkills) { this.extractedSkills = extractedSkills; }
        public String getExtractedSummary() { return extractedSummary; }
        public void setExtractedSummary(String extractedSummary) { this.extractedSummary = extractedSummary; }
        public BigDecimal getExtractedCgpa() { return extractedCgpa; }
        public void setExtractedCgpa(BigDecimal extractedCgpa) { this.extractedCgpa = extractedCgpa; }
    }

    public StudentProfileResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStudentRegistrationNumber() { return studentRegistrationNumber; }
    public void setStudentRegistrationNumber(String studentRegistrationNumber) { this.studentRegistrationNumber = studentRegistrationNumber; }
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }
    public BigDecimal getCgpa() { return cgpa; }
    public void setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; }
    public Integer getActiveBacklogs() { return activeBacklogs; }
    public void setActiveBacklogs(Integer activeBacklogs) { this.activeBacklogs = activeBacklogs; }
    public String getResumeFilePath() { return resumeFilePath; }
    public void setResumeFilePath(String resumeFilePath) { this.resumeFilePath = resumeFilePath; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getGithubProfileUrl() { return githubProfileUrl; }
    public void setGithubProfileUrl(String githubProfileUrl) { this.githubProfileUrl = githubProfileUrl; }
    public String getLinkedinProfileUrl() { return linkedinProfileUrl; }
    public void setLinkedinProfileUrl(String linkedinProfileUrl) { this.linkedinProfileUrl = linkedinProfileUrl; }
    public List<SkillItem> getSkills() { return skills; }
    public void setSkills(List<SkillItem> skills) { this.skills = skills; }
    public ResumeSummary getResumeSummary() { return resumeSummary; }
    public void setResumeSummary(ResumeSummary resumeSummary) { this.resumeSummary = resumeSummary; }
}
