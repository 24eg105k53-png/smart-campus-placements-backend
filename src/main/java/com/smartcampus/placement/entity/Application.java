package com.smartcampus.placement.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "applications",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_student_job_application", columnNames = {"student_profile_id", "job_posting_id"})
    }
)
public class Application extends BaseEntity {

    public enum ApplicationStatus {
        APPLIED,
        SHORTLISTED,
        INTERVIEW_SCHEDULED,
        REJECTED,
        PLACED
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "recruiter_notes", columnDefinition = "TEXT")
    private String recruiterNotes;

    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt = Instant.now();

    public Application() {
    }

    public Application(StudentProfile studentProfile, JobPosting jobPosting) {
        this.studentProfile = studentProfile;
        this.jobPosting = jobPosting;
        this.status = ApplicationStatus.APPLIED;
        this.appliedAt = Instant.now();
    }

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }

    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getRecruiterNotes() {
        return recruiterNotes;
    }

    public void setRecruiterNotes(String recruiterNotes) {
        this.recruiterNotes = recruiterNotes;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }
}
