
package com.devtask.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.web.cors.*;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwt) throws Exception {

        http.csrf(org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer::disable)
            .cors(cors -> {})
            .sessionManagement(s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs",
                    "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .exceptionHandling(e -> e
                .authenticationEntryPoint((request,response,exception) -> {
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Authentication required\"}");
                })
                .accessDeniedHandler((request,response,exception) -> {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Access denied\"}");
                }))
            .addFilterBefore(
                jwt,
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOriginPatterns(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource s =
            new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);

        return s;
    }
}
