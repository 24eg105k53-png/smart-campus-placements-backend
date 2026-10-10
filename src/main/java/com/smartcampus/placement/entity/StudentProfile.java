package com.smartcampus.placement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student_profiles")
public class StudentProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "student_registration_number", nullable = false, unique = true, length = 60)
    private String studentRegistrationNumber;

    @Column(name = "university", nullable = false, length = 180)
    private String university = "State Technical University";

    @Column(name = "degree", nullable = false, length = 100)
    private String degree = "B.Tech";

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "graduation_year", nullable = false)
    private Integer graduationYear;

    @Column(name = "cgpa", nullable = false, precision = 4, scale = 2)
    private BigDecimal cgpa;

    @Column(name = "active_backlogs", nullable = false)
    private Integer activeBacklogs = 0;

    @Column(name = "resume_file_path", length = 255)
    private String resumeFilePath;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "github_profile_url", length = 255)
    private String githubProfileUrl;

    @Column(name = "linkedin_profile_url", length = 255)
    private String linkedinProfileUrl;

    @OneToMany(mappedBy = "studentProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentSkill> studentSkills = new HashSet<>();

    @OneToOne(mappedBy = "studentProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private ResumeAnalysis resumeAnalysis;

    public StudentProfile() {
    }

    public StudentProfile(User user, String studentRegistrationNumber, String department, Integer graduationYear, BigDecimal cgpa, Integer activeBacklogs) {
        this.user = user;
        this.studentRegistrationNumber = studentRegistrationNumber;
        this.department = department;
        this.graduationYear = graduationYear;
        this.cgpa = cgpa;
        this.activeBacklogs = activeBacklogs != null ? activeBacklogs : 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStudentRegistrationNumber() {
        return studentRegistrationNumber;
    }

    public void setStudentRegistrationNumber(String studentRegistrationNumber) {
        this.studentRegistrationNumber = studentRegistrationNumber;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(BigDecimal cgpa) {
        this.cgpa = cgpa;
    }

    public Integer getActiveBacklogs() {
        return activeBacklogs;
    }

    public void setActiveBacklogs(Integer activeBacklogs) {
        this.activeBacklogs = activeBacklogs;
    }

    public String getResumeFilePath() {
        return resumeFilePath;
    }

    public void setResumeFilePath(String resumeFilePath) {
        this.resumeFilePath = resumeFilePath;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGithubProfileUrl() {
        return githubProfileUrl;
    }

    public void setGithubProfileUrl(String githubProfileUrl) {
        this.githubProfileUrl = githubProfileUrl;
    }

    public String getLinkedinProfileUrl() {
        return linkedinProfileUrl;
    }

    public void setLinkedinProfileUrl(String linkedinProfileUrl) {
        this.linkedinProfileUrl = linkedinProfileUrl;
    }

    public Set<StudentSkill> getStudentSkills() {
        return studentSkills;
    }

    public void setStudentSkills(Set<StudentSkill> studentSkills) {
        this.studentSkills = studentSkills;
    }

    public ResumeAnalysis getResumeAnalysis() {
        return resumeAnalysis;
    }

    public void setResumeAnalysis(ResumeAnalysis resumeAnalysis) {
        this.resumeAnalysis = resumeAnalysis;
    }
}
