package com.smartcampus.placement.service.ai;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DocumentParserService {

    private static final Logger log = LoggerFactory.getLogger(DocumentParserService.class);

    private static final Map<String, List<String>> SECTION_KEYWORDS = Map.of(
        "EDUCATION", List.of("education", "academic", "university", "college", "degree", "b.tech", "cgpa"),
        "SKILLS", List.of("skills", "technical skills", "competencies", "technologies", "tools", "languages"),
        "PROJECTS", List.of("projects", "academic projects", "personal projects", "capstone"),
        "EXPERIENCE", List.of("experience", "internship", "employment", "work experience", "training"),
        "CERTIFICATIONS", List.of("certifications", "licenses", "courses", "achievements")
    );

    private static final List<Pattern> CGPA_PATTERNS = List.of(
        Pattern.compile("(?:cgpa|gpa|pointer)\\s*[:=\\-]?\\s*([0-9]\\.[0-9]{1,2})", Pattern.CASE_INSENSITIVE),
        Pattern.compile("([0-9]\\.[0-9]{1,2})\\s*/\\s*10(?:\\.0)?", Pattern.CASE_INSENSITIVE),
        Pattern.compile("([0-9]\\.[0-9]{1,2})\\s*(?:cgpa|gpa)", Pattern.CASE_INSENSITIVE)
    );

    public ParsedDocument parse(MultipartFile file) {
        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document.pdf";
        String fileType = filename.toLowerCase().endsWith(".docx") ? "DOCX" : "PDF";
        String text = extractText(file);

        List<String> sections = detectSections(text);
        BigDecimal extractedCgpa = extractCgpa(text);

        return new ParsedDocument(filename, fileType, text, sections, extractedCgpa);
    }

    public String extractText(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            String filename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
            if (filename.endsWith(".pdf") || isPdfStream(file)) {
                try (PDDocument document = PDDocument.load(is)) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    return stripper.getText(document);
                }
            } else {
                // Read text / fallback
                byte[] bytes = file.getBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (Exception ex) {
            log.warn("Direct PDFBox document parsing encountered an issue: {}. Using heuristic text extraction.", ex.getMessage());
            try {
                return new String(file.getBytes(), StandardCharsets.ISO_8859_1);
            } catch (Exception e) {
                return "";
            }
        }
    }

    private boolean isPdfStream(MultipartFile file) {
        try {
            byte[] header = new byte[4];
            try (InputStream is = file.getInputStream()) {
                int read = is.read(header);
                return read >= 4 && new String(header, StandardCharsets.US_ASCII).startsWith("%PDF");
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> detectSections(String text) {
        if (text == null || text.isBlank()) return List.of();
        String lowerText = text.toLowerCase();
        List<String> detected = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : SECTION_KEYWORDS.entrySet()) {
            for (String kw : entry.getValue()) {
                if (lowerText.contains(kw)) {
                    detected.add(entry.getKey());
                    break;
                }
            }
        }
        return detected;
    }

    public BigDecimal extractCgpa(String text) {
        if (text == null || text.isBlank()) return new BigDecimal("8.00");

        for (Pattern p : CGPA_PATTERNS) {
            Matcher matcher = p.matcher(text);
            if (matcher.find()) {
                try {
                    double val = Double.parseDouble(matcher.group(1));
                    if (val >= 0.0 && val <= 10.0) {
                        return BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_UP);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return new BigDecimal("8.00");
    }

    public record ParsedDocument(
        String fileName,
        String fileType,
        String text,
        List<String> detectedSections,
        BigDecimal extractedCgpa
    ) {}
}
