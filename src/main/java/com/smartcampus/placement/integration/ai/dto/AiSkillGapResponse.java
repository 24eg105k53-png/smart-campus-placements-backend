package com.smartcampus.placement.integration.ai.dto;

import java.util.List;

public class AiSkillGapResponse {

    private double matchPercentage;
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private List<String> extraSkills;
    private Double cosineSimilarity;
    private String explanation;

    public AiSkillGapResponse() {}

    public double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(double matchPercentage) { this.matchPercentage = matchPercentage; }
    public List<String> getMatchingSkills() { return matchingSkills; }
    public void setMatchingSkills(List<String> matchingSkills) { this.matchingSkills = matchingSkills; }
    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }
    public List<String> getExtraSkills() { return extraSkills; }
    public void setExtraSkills(List<String> extraSkills) { this.extraSkills = extraSkills; }
    public Double getCosineSimilarity() { return cosineSimilarity; }
    public void setCosineSimilarity(Double cosineSimilarity) { this.cosineSimilarity = cosineSimilarity; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
