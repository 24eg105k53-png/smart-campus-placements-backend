package com.smartcampus.placement.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class JobPostingResponse {

    private Long id;
    private Long companyId;
    private String companyName;
    private String companyIndustry;
    private String title;
    private String description;
    private String jobLocation;
    private BigDecimal minimumCgpa;
    private Integer maximumBacklogs;
    private String eligibleDepartments;
    private Integer targetGraduationYear;
    private String ctcRange;
    private Instant applicationDeadline;
    private String status;
    private List<String> requiredSkills;
    private Long applicantsCount;
    private Boolean isEligibleForCurrentUser; // Populated if student is logged in
    private Boolean hasApplied; // Populated if student is logged in

    public JobPostingResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyIndustry() { return companyIndustry; }
    public void setCompanyIndustry(String companyIndustry) { this.companyIndustry = companyIndustry; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getJobLocation() { return jobLocation; }
    public void setJobLocation(String jobLocation) { this.jobLocation = jobLocation; }
    public BigDecimal getMinimumCgpa() { return minimumCgpa; }
    public void setMinimumCgpa(BigDecimal minimumCgpa) { this.minimumCgpa = minimumCgpa; }
    public Integer getMaximumBacklogs() { return maximumBacklogs; }
    public void setMaximumBacklogs(Integer maximumBacklogs) { this.maximumBacklogs = maximumBacklogs; }
    public String getEligibleDepartments() { return eligibleDepartments; }
    public void setEligibleDepartments(String eligibleDepartments) { this.eligibleDepartments = eligibleDepartments; }
    public Integer getTargetGraduationYear() { return targetGraduationYear; }
    public void setTargetGraduationYear(Integer targetGraduationYear) { this.targetGraduationYear = targetGraduationYear; }
    public String getCtcRange() { return ctcRange; }
    public void setCtcRange(String ctcRange) { this.ctcRange = ctcRange; }
    public Instant getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(Instant applicationDeadline) { this.applicationDeadline = applicationDeadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
    public Long getApplicantsCount() { return applicantsCount; }
    public void setApplicantsCount(Long applicantsCount) { this.applicantsCount = applicantsCount; }
    public Boolean getIsEligibleForCurrentUser() { return isEligibleForCurrentUser; }
    public void setIsEligibleForCurrentUser(Boolean isEligibleForCurrentUser) { this.isEligibleForCurrentUser = isEligibleForCurrentUser; }
    public Boolean getHasApplied() { return hasApplied; }
    public void setHasApplied(Boolean hasApplied) { this.hasApplied = hasApplied; }
}
