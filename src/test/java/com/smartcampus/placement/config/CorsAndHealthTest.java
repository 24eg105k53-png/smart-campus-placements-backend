package com.smartcampus.placement.config;

import com.smartcampus.placement.controller.HealthController;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CorsAndHealthTest {

    @Test
    void testHealthEndpoint() {
        HealthController controller = new HealthController();
        ResponseEntity<Map<String, Object>> response = controller.getHealth();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("smart-campus-placement-backend", response.getBody().get("service"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void testCorsConfigurationWithVercelAndCustomOrigins() {
        CorsConfig corsConfig = new CorsConfig();
        ReflectionTestUtils.setField(
                corsConfig,
                "allowedOrigins",
                "https://smart-campus-placements-frontend.vercel.app, https://custom-domain.com"
        );

        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        assertNotNull(source);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/auth/login");

        CorsConfiguration config = source.getCorsConfiguration(request);
        assertNotNull(config);

        assertTrue(config.getAllowCredentials());
        assertTrue(config.getAllowedOriginPatterns().contains("https://smart-campus-placements-frontend.vercel.app"));
        assertTrue(config.getAllowedOriginPatterns().contains("https://custom-domain.com"));
        assertTrue(config.getAllowedOriginPatterns().contains("https://*.vercel.app"));
        assertTrue(config.getAllowedOriginPatterns().contains("http://localhost:3000"));

        assertTrue(config.getAllowedMethods().contains("GET"));
        assertTrue(config.getAllowedMethods().contains("POST"));
        assertTrue(config.getAllowedMethods().contains("OPTIONS"));

        assertTrue(config.getAllowedHeaders().contains("*"));
    }
}
