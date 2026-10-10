package com.smartcampus.placement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/db")
public class DatabaseStatusController {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public DatabaseStatusController(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getDatabaseStatus() {
        Map<String, Object> response = new LinkedHashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            response.put("databaseStatus", "CONNECTED");
            response.put("databaseProduct", metaData.getDatabaseProductName());
            response.put("databaseVersion", metaData.getDatabaseProductVersion());
            response.put("driverName", metaData.getDriverName());
            response.put("driverVersion", metaData.getDriverVersion());
            response.put("jdbcUrl", metaData.getURL());

            // Run live SQL query verification
            Integer ping = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            response.put("sqlPing", ping != null && ping == 1 ? "SUCCESS" : "FAILED");

            // Query table record counts via SQL
            Map<String, Long> tableCounts = new LinkedHashMap<>();
            tableCounts.put("users", countRowsSafe("users"));
            tableCounts.put("companies", countRowsSafe("companies"));
            tableCounts.put("student_profiles", countRowsSafe("student_profiles"));
            tableCounts.put("skills", countRowsSafe("skills"));
            tableCounts.put("job_postings", countRowsSafe("job_postings"));
            tableCounts.put("applications", countRowsSafe("applications"));
            tableCounts.put("placement_records", countRowsSafe("placement_records"));

            response.put("tables", tableCounts);
            response.put("consoleUrl", "http://localhost:8080/h2-console");

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("databaseStatus", "ERROR");
            response.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    private Long countRowsSafe(String tableName) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }
}
