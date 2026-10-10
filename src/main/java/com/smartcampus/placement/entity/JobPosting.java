package com.smartcampus.placement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_postings")
public class JobPosting extends BaseEntity {

    public enum JobStatus {
        DRAFT,
        PUBLISHED,
        CLOSED,
        ARCHIVED
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "title", nullable = false, length = 180)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "job_location", length = 120)
    private String jobLocation = "On-Campus / Hybrid";

    @Column(name = "minimum_cgpa", nullable = false, precision = 4, scale = 2)
    private BigDecimal minimumCgpa = new BigDecimal("6.00");

    @Column(name = "maximum_backlogs", nullable = false)
    private Integer maximumBacklogs = 0;

    @Column(name = "eligible_departments", nullable = false, columnDefinition = "TEXT")
    private String eligibleDepartments; // Comma-separated departments

    @Column(name = "target_graduation_year", nullable = false)
    private Integer targetGraduationYear;

    @Column(name = "ctc_range", length = 80)
    private String ctcRange;

    @Column(name = "application_deadline", nullable = false)
    private Instant applicationDeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private JobStatus status = JobStatus.PUBLISHED;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "job_skills",
        joinColumns = @JoinColumn(name = "job_posting_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> requiredSkills = new HashSet<>();

    public JobPosting() {
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public BigDecimal getMinimumCgpa() {
        return minimumCgpa;
    }

    public void setMinimumCgpa(BigDecimal minimumCgpa) {
        this.minimumCgpa = minimumCgpa;
    }

    public Integer getMaximumBacklogs() {
        return maximumBacklogs;
    }

    public void setMaximumBacklogs(Integer maximumBacklogs) {
        this.maximumBacklogs = maximumBacklogs;
    }

    public String getEligibleDepartments() {
        return eligibleDepartments;
    }

    public void setEligibleDepartments(String eligibleDepartments) {
        this.eligibleDepartments = eligibleDepartments;
    }

    public Integer getTargetGraduationYear() {
        return targetGraduationYear;
    }

    public void setTargetGraduationYear(Integer targetGraduationYear) {
        this.targetGraduationYear = targetGraduationYear;
    }

    public String getCtcRange() {
        return ctcRange;
    }

    public void setCtcRange(String ctcRange) {
        this.ctcRange = ctcRange;
    }

    public Instant getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(Instant applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Set<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<Skill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
