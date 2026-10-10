package com.smartcampus.placement.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true, length = 180)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "industry", length = 100)
    private String industry;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_user_id", unique = true)
    private User recruiterUser;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobPosting> jobPostings = new ArrayList<>();

    public Company() {
    }

    public Company(String name, String description, String website, String industry, User recruiterUser) {
        this.name = name;
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.recruiterUser = recruiterUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public User getRecruiterUser() {
        return recruiterUser;
    }

    public void setRecruiterUser(User recruiterUser) {
        this.recruiterUser = recruiterUser;
    }

    public List<JobPosting> getJobPostings() {
        return jobPostings;
    }

    public void setJobPostings(List<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }
}
