package com.anpetna.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import java.util.Arrays;
@org.springframework.context.annotation.Configuration
public class DebugConfig {
    @Bean
    ApplicationRunner showEffectiveConfig(Environment env) {
        return args -> {
            System.out.println("=== EFFECTIVE SPRING CONFIG ===");
            System.out.println("ActiveProfiles = " + Arrays.toString(env.getActiveProfiles()));
            System.out.println("spring.datasource.url = " + env.getProperty("spring.datasource.url"));
            System.out.println("spring.datasource.username = " + env.getProperty("spring.datasource.username"));
            System.out.println("spring.datasource.driver-class-name = " + env.getProperty("spring.datasource.driver-class-name"));
            System.out.println("================================");
        };
    }
}
