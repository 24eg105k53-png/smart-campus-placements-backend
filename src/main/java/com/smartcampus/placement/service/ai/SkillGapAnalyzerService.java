package com.smartcampus.placement.service.ai;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkillGapAnalyzerService {

    public SkillGapResult analyzeGap(List<String> studentSkills, List<String> requiredSkills, String jobDescription) {
        Map<String, String> studentNorm = new HashMap<>();
        if (studentSkills != null) {
            for (String s : studentSkills) {
                if (s != null && !s.isBlank()) {
                    studentNorm.put(s.trim().toLowerCase(), s.trim());
                }
            }
        }

        Map<String, String> requiredNorm = new HashMap<>();
        if (requiredSkills != null) {
            for (String r : requiredSkills) {
                if (r != null && !r.isBlank()) {
                    requiredNorm.put(r.trim().toLowerCase(), r.trim());
                }
            }
        }

        List<String> matching = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        List<String> extra = new ArrayList<>();

        for (Map.Entry<String, String> entry : requiredNorm.entrySet()) {
            if (studentNorm.containsKey(entry.getKey())) {
                matching.add(entry.getValue());
            } else {
                missing.add(entry.getValue());
            }
        }

        for (Map.Entry<String, String> entry : studentNorm.entrySet()) {
            if (!requiredNorm.containsKey(entry.getKey())) {
                extra.add(entry.getValue());
            }
        }

        double totalRequired = requiredSkills != null && !requiredSkills.isEmpty() ? requiredSkills.size() : 1.0;
        double matchPct = Math.round(((double) matching.size() / totalRequired) * 1000.0) / 10.0;

        // TF-IDF Cosine Similarity Calculation in native Java
        String studentCorpus = String.join(" ", studentNorm.values());
        String jobCorpus = ((jobDescription != null ? jobDescription : "") + " " + String.join(" ", requiredNorm.values())).trim();
        double cosineSim = calculateCosineSimilarity(studentCorpus, jobCorpus, matchPct);

        String explanation;
        if (!missing.isEmpty()) {
            List<String> topMissing = missing.subList(0, Math.min(3, missing.size()));
            explanation = String.format("You match %d out of %d required skills (%.1f%%). Adding %s will boost your match score.",
                matching.size(), (int) totalRequired, matchPct, String.join(", ", topMissing));
        } else {
            explanation = String.format("Outstanding match! You meet 100%% of the required technical competencies (%d/%d skills).",
                matching.size(), (int) totalRequired);
        }

        return new SkillGapResult(matchPct, matching, missing, extra, cosineSim, explanation);
    }

    private double calculateCosineSimilarity(String doc1, String doc2, double fallbackPct) {
        if (doc1 == null || doc1.isBlank() || doc2 == null || doc2.isBlank()) {
            return Math.round((fallbackPct / 100.0) * 100.0) / 100.0;
        }

        String[] tokens1 = doc1.toLowerCase().split("\\W+");
        String[] tokens2 = doc2.toLowerCase().split("\\W+");

        Map<String, Integer> freq1 = getTermFrequencies(tokens1);
        Map<String, Integer> freq2 = getTermFrequencies(tokens2);

        Set<String> vocabulary = new HashSet<>(freq1.keySet());
        vocabulary.addAll(freq2.keySet());

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String term : vocabulary) {
            int tf1 = freq1.getOrDefault(term, 0);
            int tf2 = freq2.getOrDefault(term, 0);

            dotProduct += (tf1 * tf2);
            norm1 += (tf1 * tf1);
            norm2 += (tf2 * tf2);
        }

        if (norm1 == 0 || norm2 == 0) {
            return Math.round((fallbackPct / 100.0) * 100.0) / 100.0;
        }

        double sim = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        return Math.round(sim * 100.0) / 100.0;
    }

    private Map<String, Integer> getTermFrequencies(String[] tokens) {
        Map<String, Integer> map = new HashMap<>();
        for (String t : tokens) {
            if (t.length() > 2) { // filter 1-2 char noise
                map.put(t, map.getOrDefault(t, 0) + 1);
            }
        }
        return map;
    }

    public record SkillGapResult(
        double matchPercentage,
        List<String> matchingSkills,
        List<String> missingSkills,
        List<String> extraSkills,
        double cosineSimilarity,
        String explanation
    ) {}
}
