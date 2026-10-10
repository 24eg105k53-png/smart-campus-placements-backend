package com.smartcampus.placement.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class JobPostingCreateRequest {

    private Long companyId; // If created by TPO, or null if created by recruiter

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    private String jobLocation = "On-Campus / Hybrid";

    @NotNull(message = "Minimum CGPA is required")
    @DecimalMin("0.00")
    @DecimalMax("10.00")
    private BigDecimal minimumCgpa = new BigDecimal("6.00");

    @NotNull(message = "Maximum backlogs is required")
    @Min(0)
    private Integer maximumBacklogs = 0;

    @NotBlank(message = "Eligible departments are required")
    private String eligibleDepartments; // Comma-separated departments

    @NotNull(message = "Target graduation year is required")
    private Integer targetGraduationYear;

    private String ctcRange;

    @NotNull(message = "Application deadline is required")
    private Instant applicationDeadline;

    private List<String> requiredSkills;

    public JobPostingCreateRequest() {}

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

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

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
}
