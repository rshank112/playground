package com.sample.school.config;

import com.sample.school.ratelimiter.LimitFilter;
import com.sample.school.security.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
    private final AuthFilter jwtFilter;
    private final LimitFilter rateLimitFilter;

    public SecurityConfig(AuthFilter jwtFilter, LimitFilter rateLimitFilter) {
        this.jwtFilter = jwtFilter;
        this.rateLimitFilter = rateLimitFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/school/auth/**", "/login.html",
                                "/template/error/**", "/index.html", "/429.html",
                                "/", "/app.js", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable) // disable Spring's default login page
                .httpBasic(AbstractHttpConfigurer::disable); // disable basic auth

        // Add rate limiting *before* JWT
        http.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class);

        // Add JWT filter
        http.addFilterAfter(jwtFilter, LimitFilter.class);

        return http.build();
    }


}
