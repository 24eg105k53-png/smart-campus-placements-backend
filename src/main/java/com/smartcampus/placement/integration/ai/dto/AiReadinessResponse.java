package com.smartcampus.placement.integration.ai.dto;

import java.util.List;
import java.util.Map;

public class AiReadinessResponse {

    private int readinessScore;
    private String readinessTier;
    private Map<String, Integer> breakdown;
    private List<String> recommendations;

    public AiReadinessResponse() {}

    public int getReadinessScore() { return readinessScore; }
    public void setReadinessScore(int readinessScore) { this.readinessScore = readinessScore; }

    public String getReadinessTier() { return readinessTier; }
    public void setReadinessTier(String readinessTier) { this.readinessTier = readinessTier; }

    public Map<String, Integer> getBreakdown() { return breakdown; }
    public void setBreakdown(Map<String, Integer> breakdown) { this.breakdown = breakdown; }

    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
}
