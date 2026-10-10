package com.smartcampus.placement.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class PlacementStatsResponse {

    private long totalStudents;
    private long totalPlaced;
    private double placementRatePercentage;
    private long activeDrives;
    private long totalApplications;
    private BigDecimal averageCtc;
    private BigDecimal highestCtc;
    private List<BranchPlacementStatsResponse> branchStats;

    public PlacementStatsResponse() {}

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getTotalPlaced() { return totalPlaced; }
    public void setTotalPlaced(long totalPlaced) { this.totalPlaced = totalPlaced; }

    public double getPlacementRatePercentage() { return placementRatePercentage; }
    public void setPlacementRatePercentage(double placementRatePercentage) { this.placementRatePercentage = placementRatePercentage; }

    public long getActiveDrives() { return activeDrives; }
    public void setActiveDrives(long activeDrives) { this.activeDrives = activeDrives; }

    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }

    public BigDecimal getAverageCtc() { return averageCtc; }
    public void setAverageCtc(BigDecimal averageCtc) { this.averageCtc = averageCtc; }

    public BigDecimal getHighestCtc() { return highestCtc; }
    public void setHighestCtc(BigDecimal highestCtc) { this.highestCtc = highestCtc; }

    public List<BranchPlacementStatsResponse> getBranchStats() { return branchStats; }
    public void setBranchStats(List<BranchPlacementStatsResponse> branchStats) { this.branchStats = branchStats; }
}
