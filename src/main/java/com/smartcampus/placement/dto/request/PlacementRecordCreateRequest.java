package com.smartcampus.placement.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PlacementRecordCreateRequest {

    @NotNull(message = "Student Profile ID is required")
    private Long studentProfileId;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    private Long jobPostingId;

    @NotNull(message = "Placement year is required")
    private Integer placementYear;

    @NotNull(message = "CTC amount is required")
    @DecimalMin(value = "0.00", message = "CTC amount must be non-negative")
    private BigDecimal ctcAmount;

    public PlacementRecordCreateRequest() {}

    public Long getStudentProfileId() { return studentProfileId; }
    public void setStudentProfileId(Long studentProfileId) { this.studentProfileId = studentProfileId; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public Long getJobPostingId() { return jobPostingId; }
    public void setJobPostingId(Long jobPostingId) { this.jobPostingId = jobPostingId; }

    public Integer getPlacementYear() { return placementYear; }
    public void setPlacementYear(Integer placementYear) { this.placementYear = placementYear; }

    public BigDecimal getCtcAmount() { return ctcAmount; }
    public void setCtcAmount(BigDecimal ctcAmount) { this.ctcAmount = ctcAmount; }
}
