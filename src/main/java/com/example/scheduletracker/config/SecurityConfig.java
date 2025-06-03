// src/main/java/com/example/scheduletracker/config/SecurityConfig.java
package com.example.scheduletracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF для простоты (только в dev!)
                .csrf(csrf -> csrf.disable())
                // Разрешаем без логина доступ к Swagger и публичным GET-эндпоинтам
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/favicon.ico",
                                "/",       // если хотите, чтобы корень был публичным
                                "/api/teachers/**",
                                "/api/groups/**",
                                "/api/lessons/**"
                        ).permitAll()
                        // всё остальное — под логин в
                        .anyRequest().authenticated()
                )
                // включаем HTTP Basic для остальных
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
