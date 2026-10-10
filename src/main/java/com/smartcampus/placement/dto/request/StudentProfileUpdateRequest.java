package com.smartcampus.placement.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class StudentProfileUpdateRequest {

    private String phoneNumber;
    private String githubProfileUrl;
    private String linkedinProfileUrl;

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "10.00")
    private BigDecimal cgpa;

    @Min(0)
    private Integer activeBacklogs;

    private List<String> skills;

    public StudentProfileUpdateRequest() {}

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getGithubProfileUrl() { return githubProfileUrl; }
    public void setGithubProfileUrl(String githubProfileUrl) { this.githubProfileUrl = githubProfileUrl; }

    public String getLinkedinProfileUrl() { return linkedinProfileUrl; }
    public void setLinkedinProfileUrl(String linkedinProfileUrl) { this.linkedinProfileUrl = linkedinProfileUrl; }

    public BigDecimal getCgpa() { return cgpa; }
    public void setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; }

    public Integer getActiveBacklogs() { return activeBacklogs; }
    public void setActiveBacklogs(Integer activeBacklogs) { this.activeBacklogs = activeBacklogs; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}
