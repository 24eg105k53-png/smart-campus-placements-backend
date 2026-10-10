package com.smartcampus.placement.dto.request;

import com.smartcampus.placement.entity.JobPosting;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class JobPostingUpdateRequest {

    private String title;
    private String description;
    private String jobLocation;

    @DecimalMin("0.00")
    @DecimalMax("10.00")
    private BigDecimal minimumCgpa;

    @Min(0)
    private Integer maximumBacklogs;

    private String eligibleDepartments;
    private Integer targetGraduationYear;
    private String ctcRange;
    private Instant applicationDeadline;
    private JobPosting.JobStatus status;
    private List<String> requiredSkills;

    public JobPostingUpdateRequest() {}

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

    public JobPosting.JobStatus getStatus() { return status; }
    public void setStatus(JobPosting.JobStatus status) { this.status = status; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
}
