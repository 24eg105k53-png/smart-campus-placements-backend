package com.smartcampus.placement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "placement_records")
public class PlacementRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    @Column(name = "placement_year", nullable = false)
    private Integer placementYear;

    @Column(name = "ctc_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal ctcAmount; // In LPA

    @Column(name = "offer_date", nullable = false)
    private LocalDate offerDate = LocalDate.now();

    @Column(name = "verified_by_tpo", nullable = false)
    private Boolean verifiedByTpo = true;

    public PlacementRecord() {
    }

    public PlacementRecord(StudentProfile studentProfile, Company company, JobPosting jobPosting, Integer placementYear, BigDecimal ctcAmount) {
        this.studentProfile = studentProfile;
        this.company = company;
        this.jobPosting = jobPosting;
        this.placementYear = placementYear;
        this.ctcAmount = ctcAmount;
        this.offerDate = LocalDate.now();
        this.verifiedByTpo = true;
    }

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }

    public Integer getPlacementYear() {
        return placementYear;
    }

    public void setPlacementYear(Integer placementYear) {
        this.placementYear = placementYear;
    }

    public BigDecimal getCtcAmount() {
        return ctcAmount;
    }

    public void setCtcAmount(BigDecimal ctcAmount) {
        this.ctcAmount = ctcAmount;
    }

    public LocalDate getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(LocalDate offerDate) {
        this.offerDate = offerDate;
    }

    public Boolean getVerifiedByTpo() {
        return verifiedByTpo;
    }

    public void setVerifiedByTpo(Boolean verifiedByTpo) {
        this.verifiedByTpo = verifiedByTpo;
    }
}
