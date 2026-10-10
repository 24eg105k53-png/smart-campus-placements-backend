package com.smartcampus.placement.controller;

import com.smartcampus.placement.dto.request.PlacementRecordCreateRequest;
import com.smartcampus.placement.dto.response.BranchPlacementStatsResponse;
import com.smartcampus.placement.dto.response.PlacementStatsResponse;
import com.smartcampus.placement.dto.response.StudentProfileResponse;
import com.smartcampus.placement.entity.PlacementRecord;
import com.smartcampus.placement.service.TpoAnalyticsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tpo")
@PreAuthorize("hasRole('TPO')")
public class TpoController {

    private final TpoAnalyticsService tpoAnalyticsService;

    public TpoController(TpoAnalyticsService tpoAnalyticsService) {
        this.tpoAnalyticsService = tpoAnalyticsService;
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<PlacementStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(tpoAnalyticsService.getDashboardStats());
    }

    @GetMapping("/branch-analytics")
    public ResponseEntity<List<BranchPlacementStatsResponse>> getBranchAnalytics() {
        return ResponseEntity.ok(tpoAnalyticsService.getBranchAnalytics());
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentProfileResponse>> getAllStudents(
            @RequestParam(required = false) String department) {
        return ResponseEntity.ok(tpoAnalyticsService.getAllStudents(department));
    }

    @PostMapping("/placements")
    public ResponseEntity<PlacementRecord> recordPlacement(@Valid @RequestBody PlacementRecordCreateRequest request) {
        PlacementRecord record = tpoAnalyticsService.recordPlacement(request);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    @GetMapping("/placements")
    public ResponseEntity<List<PlacementRecord>> getAllPlacements() {
        return ResponseEntity.ok(tpoAnalyticsService.getAllPlacements());
    }
}
