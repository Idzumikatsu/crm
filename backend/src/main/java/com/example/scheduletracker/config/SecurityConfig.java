// src/main/java/com/example/scheduletracker/config/SecurityConfig.java
package com.example.scheduletracker.config;

import com.example.scheduletracker.config.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  private final UserDetailsService userDetailsService;
  private final JwtFilter jwtFilter;

  public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/login",
                        "/login.html",
                        "/index.html",
                        "/",
                        "/api/auth/login",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/actuator/**")
                    .permitAll()
                    .requestMatchers("/api/teachers/**", "/api/students/**", "/api/manager/**")
                    .hasAnyRole("MANAGER", "ADMIN")
                    .requestMatchers("/api/teacher/**")
                    .hasAnyRole("TEACHER", "ADMIN")
                    .anyRequest()
                    .authenticated())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .userDetailsService(userDetailsService)
        .httpBasic(httpBasic -> {})
        .formLogin(
            fl ->
                fl.loginPage("/login")
                    .successHandler(
                        (req, res, auth) -> {
                          var roles = auth.getAuthorities();
                          if (roles.stream()
                              .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
                            res.sendRedirect("/manager");
                          } else {
                            res.sendRedirect("/teacher");
                          }
                        })
                    .permitAll())
        .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, ex2) -> res.sendError(401)))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
