package com.smartcampus.placement.service.ai;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ReadinessEngineService {

    public ReadinessResult evaluateReadiness(BigDecimal cgpa, int activeBacklogs, int skillsCount, boolean hasResume, int appliedJobsCount) {
        double cgpaVal = cgpa != null ? cgpa.doubleValue() : 7.0;

        // 1. Academic Score (Max 30)
        int academicScore;
        if (cgpaVal >= 8.5) {
            academicScore = 30;
        } else if (cgpaVal >= 7.5) {
            academicScore = 25;
        } else if (cgpaVal >= 6.5) {
            academicScore = 20;
        } else {
            academicScore = 15;
        }

        if (activeBacklogs > 0) {
            academicScore = Math.max(5, academicScore - (activeBacklogs * 10));
        }

        // 2. Skills Score (Max 40)
        int skillsScore = Math.min(40, skillsCount * 6);

        // 3. Completeness & Engagement (Max 30)
        int profileScore = (hasResume ? 20 : 5) + Math.min(10, appliedJobsCount * 3);

        int totalScore = Math.min(100, academicScore + skillsScore + profileScore);

        String tier;
        if (totalScore >= 80) {
            tier = "HIGH_READINESS";
        } else if (totalScore >= 60) {
            tier = "MODERATE_READINESS";
        } else {
            tier = "NEEDS_IMPROVEMENT";
        }

        List<String> recommendations = new ArrayList<>();
        if (activeBacklogs > 0) {
            recommendations.add(String.format("Clear your %d active backlog(s) to pass eligibility filters for tier-1 companies.", activeBacklogs));
        }
        if (skillsCount < 6) {
            recommendations.add("Add more in-demand technical skills (e.g., Docker, PostgreSQL, React, AWS) to enhance resume score.");
        }
        if (!hasResume) {
            recommendations.add("Upload a parsed PDF/DOCX resume to unlock AI skill validation.");
        }
        if (appliedJobsCount == 0) {
            recommendations.add("Explore active placement drives and apply to eligible openings.");
        }
        if (recommendations.isEmpty()) {
            recommendations.add("Excellent academic standing and skill breadth! Prepare for technical viva and system design interviews.");
        }

        Map<String, Integer> breakdown = Map.of(
            "academicScore", academicScore,
            "maxAcademic", 30,
            "skillsScore", skillsScore,
            "maxSkills", 40,
            "profileCompletenessScore", profileScore,
            "maxCompleteness", 30
        );

        return new ReadinessResult(totalScore, tier, breakdown, recommendations);
    }

    public record ReadinessResult(
        int readinessScore,
        String readinessTier,
        Map<String, Integer> breakdown,
        List<String> recommendations
    ) {}
}
