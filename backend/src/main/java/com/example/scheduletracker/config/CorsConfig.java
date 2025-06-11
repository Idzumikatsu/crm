package com.example.scheduletracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/api/**")
        .allowedOrigins(
            "http://crm-synergy.ru",
            "https://crm-synergy.ru",
            "http://www.crm-synergy.ru",
            "https://www.crm-synergy.ru")
        .allowedMethods("*");
  }
}
