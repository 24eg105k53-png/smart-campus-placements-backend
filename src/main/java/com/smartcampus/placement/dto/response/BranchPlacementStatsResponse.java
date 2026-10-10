package com.smartcampus.placement.dto.response;

import java.math.BigDecimal;

public class BranchPlacementStatsResponse {

    private String department;
    private long totalStudents;
    private long placedStudents;
    private double placementPercentage;
    private BigDecimal averageCtc;

    public BranchPlacementStatsResponse() {}

    public BranchPlacementStatsResponse(String department, long totalStudents, long placedStudents, double placementPercentage, BigDecimal averageCtc) {
        this.department = department;
        this.totalStudents = totalStudents;
        this.placedStudents = placedStudents;
        this.placementPercentage = placementPercentage;
        this.averageCtc = averageCtc;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getPlacedStudents() { return placedStudents; }
    public void setPlacedStudents(long placedStudents) { this.placedStudents = placedStudents; }

    public double getPlacementPercentage() { return placementPercentage; }
    public void setPlacementPercentage(double placementPercentage) { this.placementPercentage = placementPercentage; }

    public BigDecimal getAverageCtc() { return averageCtc; }
    public void setAverageCtc(BigDecimal averageCtc) { this.averageCtc = averageCtc; }
}
