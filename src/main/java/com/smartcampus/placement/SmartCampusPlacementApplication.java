package com.smartcampus.placement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartCampusPlacementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartCampusPlacementApplication.class, args);
    }
}
