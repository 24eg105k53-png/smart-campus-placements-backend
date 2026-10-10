package com.smartcampus.placement.service.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class SkillExtractorService {

    private static final Logger log = LoggerFactory.getLogger(SkillExtractorService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> skills = new ArrayList<>();
    private final Map<String, String> aliases = new HashMap<>();
    private final Map<String, Pattern> skillPatterns = new HashMap<>();
    private final Map<String, Pattern> aliasPatterns = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("data/skills_dictionary.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    Map<String, Object> data = objectMapper.readValue(is, new TypeReference<>() {});
                    if (data.containsKey("skills")) {
                        @SuppressWarnings("unchecked")
                        List<String> loadedSkills = (List<String>) data.get("skills");
                        skills.addAll(loadedSkills);
                    }
                    if (data.containsKey("aliases")) {
                        @SuppressWarnings("unchecked")
                        Map<String, String> loadedAliases = (Map<String, String>) data.get("aliases");
                        aliases.putAll(loadedAliases);
                    }
                }
            }
        } catch (Exception ex) {
            log.warn("Could not load skills dictionary from resources: {}. Using default baseline.", ex.getMessage());
        }

        if (skills.isEmpty()) {
            skills.addAll(List.of("Java", "Python", "Spring Boot", "React", "PostgreSQL", "Docker", "Git", "FastAPI", "SQL", "JavaScript", "TypeScript"));
        }

        // Precompile regex boundary patterns
        for (String skill : skills) {
            String escaped = Pattern.quote(skill.toLowerCase());
            Pattern p = Pattern.compile("(?<![a-zA-Z0-9_])" + escaped + "(?![a-zA-Z0-9_])", Pattern.CASE_INSENSITIVE);
            skillPatterns.put(skill, p);
        }

        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            String escaped = Pattern.quote(entry.getKey().toLowerCase());
            Pattern p = Pattern.compile("(?<![a-zA-Z0-9_])" + escaped + "(?![a-zA-Z0-9_])", Pattern.CASE_INSENSITIVE);
            aliasPatterns.put(entry.getKey(), p);
        }

        log.info("SkillExtractorService initialized with {} canonical skills and {} aliases.", skills.size(), aliases.size());
    }

    public List<String> extractSkills(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String lowerText = " " + text.toLowerCase() + " ";
        Set<String> matched = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        for (Map.Entry<String, Pattern> entry : skillPatterns.entrySet()) {
            if (entry.getValue().matcher(lowerText).find()) {
                matched.add(entry.getKey());
            }
        }

        for (Map.Entry<String, Pattern> entry : aliasPatterns.entrySet()) {
            if (entry.getValue().matcher(lowerText).find()) {
                matched.add(aliases.get(entry.getKey()));
            }
        }

        return new ArrayList<>(matched);
    }
}
