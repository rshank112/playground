package com.sample.school.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LimitFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // Skip static resources
        return !path.startsWith("/school/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = resolve(ip);
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            System.out.println("bucket token consume failed");
            response.setStatus(429);
            response.getWriter().write("Too many requests. Try again later.");
        }
    }

    private Bucket resolve(String key) {
        System.out.println("key is " + key);
        return cache.computeIfAbsent(key, k -> Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1))))
                .build());
    }
}