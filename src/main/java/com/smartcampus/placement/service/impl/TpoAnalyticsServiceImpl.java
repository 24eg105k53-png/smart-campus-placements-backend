package com.smartcampus.placement.service.impl;

import com.smartcampus.placement.dto.request.PlacementRecordCreateRequest;
import com.smartcampus.placement.dto.response.BranchPlacementStatsResponse;
import com.smartcampus.placement.dto.response.PlacementStatsResponse;
import com.smartcampus.placement.dto.response.StudentProfileResponse;
import com.smartcampus.placement.entity.*;
import com.smartcampus.placement.exception.ResourceNotFoundException;
import com.smartcampus.placement.repository.*;
import com.smartcampus.placement.service.StudentProfileService;
import com.smartcampus.placement.service.TpoAnalyticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TpoAnalyticsServiceImpl implements TpoAnalyticsService {

    private final StudentProfileRepository studentProfileRepository;
    private final PlacementRecordRepository placementRecordRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final StudentProfileService studentProfileService;

    public TpoAnalyticsServiceImpl(StudentProfileRepository studentProfileRepository,
                                   PlacementRecordRepository placementRecordRepository,
                                   JobPostingRepository jobPostingRepository,
                                   ApplicationRepository applicationRepository,
                                   CompanyRepository companyRepository,
                                   StudentProfileService studentProfileService) {
        this.studentProfileRepository = studentProfileRepository;
        this.placementRecordRepository = placementRecordRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
        this.studentProfileService = studentProfileService;
    }

    @Override
    @Transactional(readOnly = true)
    public PlacementStatsResponse getDashboardStats() {
        long totalStudents = studentProfileRepository.count();
        long totalPlaced = placementRecordRepository.count();
        long activeDrives = jobPostingRepository.countByStatus(JobPosting.JobStatus.PUBLISHED);
        long totalApplications = applicationRepository.count();

        BigDecimal avgCtc = placementRecordRepository.findAverageCtc();
        BigDecimal highestCtc = placementRecordRepository.findHighestCtc();

        double placementPct = totalStudents > 0
            ? Math.round(((double) totalPlaced / totalStudents) * 1000.0) / 10.0
            : 0.0;

        PlacementStatsResponse stats = new PlacementStatsResponse();
        stats.setTotalStudents(totalStudents);
        stats.setTotalPlaced(totalPlaced);
        stats.setPlacementRatePercentage(placementPct);
        stats.setActiveDrives(activeDrives);
        stats.setTotalApplications(totalApplications);
        stats.setAverageCtc(avgCtc != null ? avgCtc.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        stats.setHighestCtc(highestCtc != null ? highestCtc.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        stats.setBranchStats(getBranchAnalytics());

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchPlacementStatsResponse> getBranchAnalytics() {
        List<String> departments = studentProfileRepository.findDistinctDepartments();
        List<BranchPlacementStatsResponse> branchList = new ArrayList<>();

        for (String dept : departments) {
            long totalInDept = studentProfileRepository.countByDepartment(dept);
            List<StudentProfile> deptStudents = studentProfileRepository.findByDepartment(dept);

            long placedInDept = deptStudents.stream()
                .filter(s -> placementRecordRepository.existsByStudentProfileId(s.getId()))
                .count();

            double pct = totalInDept > 0
                ? Math.round(((double) placedInDept / totalInDept) * 1000.0) / 10.0
                : 0.0;

            branchList.add(new BranchPlacementStatsResponse(
                dept,
                totalInDept,
                placedInDept,
                pct,
                new BigDecimal("12.50") // Average benchmark
            ));
        }

        return branchList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentProfileResponse> getAllStudents(String department) {
        List<StudentProfile> students;
        if (department != null && !department.trim().isEmpty() && !department.equalsIgnoreCase("ALL")) {
            students = studentProfileRepository.findByDepartment(department.trim());
        } else {
            students = studentProfileRepository.findAll();
        }

        return students.stream()
            .map(s -> studentProfileService.getStudentProfile(s.getUser().getId()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlacementRecord recordPlacement(PlacementRecordCreateRequest request) {
        StudentProfile student = studentProfileRepository.findById(request.getStudentProfileId())
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found with ID: " + request.getStudentProfileId()));

        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.getCompanyId()));

        JobPosting job = null;
        if (request.getJobPostingId() != null) {
            job = jobPostingRepository.findById(request.getJobPostingId()).orElse(null);
        }

        PlacementRecord record = new PlacementRecord(
            student,
            company,
            job,
            request.getPlacementYear(),
            request.getCtcAmount()
        );

        return placementRecordRepository.save(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlacementRecord> getAllPlacements() {
        return placementRecordRepository.findAll();
    }
}
