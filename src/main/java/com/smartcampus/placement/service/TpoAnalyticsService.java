package com.smartcampus.placement.service;

import com.smartcampus.placement.dto.request.PlacementRecordCreateRequest;
import com.smartcampus.placement.dto.response.BranchPlacementStatsResponse;
import com.smartcampus.placement.dto.response.PlacementStatsResponse;
import com.smartcampus.placement.dto.response.StudentProfileResponse;
import com.smartcampus.placement.entity.PlacementRecord;

import java.util.List;

public interface TpoAnalyticsService {
    PlacementStatsResponse getDashboardStats();
    List<BranchPlacementStatsResponse> getBranchAnalytics();
    List<StudentProfileResponse> getAllStudents(String department);
    PlacementRecord recordPlacement(PlacementRecordCreateRequest request);
    List<PlacementRecord> getAllPlacements();
}
