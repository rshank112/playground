package com.sample.school.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collections;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // allow auth endpoints without token
        String path = request.getServletPath();
        return path.startsWith("/school/auth") || path.equals("/login.html") || path.equals("/429.html")
                || path.equals("/index.html") || path.startsWith("/template/error/")
                || path.equals("/favicon.ico");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String username = SecurityUtil.validateToken(token);
            if (username != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
