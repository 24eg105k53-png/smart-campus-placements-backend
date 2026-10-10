package com.smartcampus.placement.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class ApplicationResponse {

    private Long id;
    private Long studentProfileId;
    private String studentName;
    private String studentEmail;
    private String studentRegistrationNumber;
    private String studentDepartment;
    private BigDecimal studentCgpa;
    private Integer studentBacklogs;
    private List<String> studentSkills;
    private String studentResumeFilePath;

    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String ctcRange;

    private String status;
    private String recruiterNotes;
    private Instant appliedAt;
    private Instant updatedAt;
    private Double matchScore;

    public ApplicationResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentProfileId() { return studentProfileId; }
    public void setStudentProfileId(Long studentProfileId) { this.studentProfileId = studentProfileId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public String getStudentRegistrationNumber() { return studentRegistrationNumber; }
    public void setStudentRegistrationNumber(String studentRegistrationNumber) { this.studentRegistrationNumber = studentRegistrationNumber; }
    public String getStudentDepartment() { return studentDepartment; }
    public void setStudentDepartment(String studentDepartment) { this.studentDepartment = studentDepartment; }
    public BigDecimal getStudentCgpa() { return studentCgpa; }
    public void setStudentCgpa(BigDecimal studentCgpa) { this.studentCgpa = studentCgpa; }
    public Integer getStudentBacklogs() { return studentBacklogs; }
    public void setStudentBacklogs(Integer studentBacklogs) { this.studentBacklogs = studentBacklogs; }
    public List<String> getStudentSkills() { return studentSkills; }
    public void setStudentSkills(List<String> studentSkills) { this.studentSkills = studentSkills; }
    public String getStudentResumeFilePath() { return studentResumeFilePath; }
    public void setStudentResumeFilePath(String studentResumeFilePath) { this.studentResumeFilePath = studentResumeFilePath; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCtcRange() { return ctcRange; }
    public void setCtcRange(String ctcRange) { this.ctcRange = ctcRange; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRecruiterNotes() { return recruiterNotes; }
    public void setRecruiterNotes(String recruiterNotes) { this.recruiterNotes = recruiterNotes; }
    public Instant getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Instant appliedAt) { this.appliedAt = appliedAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Double getMatchScore() { return matchScore; }
    public void setMatchScore(Double matchScore) { this.matchScore = matchScore; }
}
