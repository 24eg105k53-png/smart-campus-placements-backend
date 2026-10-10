package com.smartcampus.placement.service;

import com.smartcampus.placement.service.ai.DocumentParserService;
import com.smartcampus.placement.service.ai.ReadinessEngineService;
import com.smartcampus.placement.service.ai.SkillExtractorService;
import com.smartcampus.placement.service.ai.SkillGapAnalyzerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NativeAiServicesTest {

    private SkillExtractorService skillExtractorService;
    private SkillGapAnalyzerService skillGapAnalyzerService;
    private ReadinessEngineService readinessEngineService;
    private DocumentParserService documentParserService;

    @BeforeEach
    void setUp() {
        skillExtractorService = new SkillExtractorService();
        skillExtractorService.init();

        skillGapAnalyzerService = new SkillGapAnalyzerService();
        readinessEngineService = new ReadinessEngineService();
        documentParserService = new DocumentParserService();
    }

    @Test
    @DisplayName("Should extract standard skills and normalize aliases natively in Java")
    void testSkillExtractorAndAliases() {
        String text = "Candidate is proficient in Java, Spring Boot, and PostgreSQL with experience using k8s and reactjs for microservices.";
        List<String> skills = skillExtractorService.extractSkills(text);

        assertTrue(skills.contains("Java"), "Should contain Java");
        assertTrue(skills.contains("Spring Boot"), "Should contain Spring Boot");
        assertTrue(skills.contains("PostgreSQL"), "Should contain PostgreSQL");
        assertTrue(skills.contains("Kubernetes"), "Should normalize k8s to Kubernetes");
        assertTrue(skills.contains("React"), "Should normalize reactjs to React");
    }

    @Test
    @DisplayName("Should analyze skill gap with TF-IDF cosine similarity in Java")
    void testSkillGapAnalysis() {
        List<String> student = List.of("Java", "Spring Boot", "Git");
        List<String> required = List.of("Java", "Spring Boot", "PostgreSQL", "Docker");

        SkillGapAnalyzerService.SkillGapResult result = skillGapAnalyzerService.analyzeGap(
            student, required, "Java and Spring Boot backend engineer"
        );

        assertEquals(50.0, result.matchPercentage());
        assertTrue(result.matchingSkills().contains("Java"));
        assertTrue(result.matchingSkills().contains("Spring Boot"));
        assertTrue(result.missingSkills().contains("PostgreSQL"));
        assertTrue(result.missingSkills().contains("Docker"));
        assertTrue(result.cosineSimilarity() > 0.0);
        assertNotNull(result.explanation());
    }

    @Test
    @DisplayName("Should calculate explainable placement readiness score in Java")
    void testReadinessCalculation() {
        ReadinessEngineService.ReadinessResult result = readinessEngineService.evaluateReadiness(
            new BigDecimal("8.80"),
            0,
            8,
            true,
            3
        );

        assertTrue(result.readinessScore() >= 80, "Score should be >= 80");
        assertEquals("HIGH_READINESS", result.readinessTier());
        assertEquals(30, result.breakdown().get("academicScore"));
        assertFalse(result.recommendations().isEmpty());
    }

    @Test
    @DisplayName("Should detect resume sections and heuristic CGPA in Java")
    void testDocumentParserHeuristics() {
        String resumeText = """
            John Doe - Computer Science Engineer
            EDUCATION:
            B.Tech in CSE with CGPA: 8.75 / 10
            SKILLS:
            Java, Spring Boot, React, SQL
            PROJECTS:
            Campus Placement Portal
            EXPERIENCE:
            Software Engineering Intern
            """;

        List<String> sections = documentParserService.detectSections(resumeText);
        assertTrue(sections.contains("EDUCATION"));
        assertTrue(sections.contains("SKILLS"));
        assertTrue(sections.contains("PROJECTS"));
        assertTrue(sections.contains("EXPERIENCE"));

        BigDecimal cgpa = documentParserService.extractCgpa(resumeText);
        assertEquals(new BigDecimal("8.75"), cgpa);
    }
}
