package com.sample.school.controller;

import com.sample.school.security.SecurityUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/school/auth")
public class StudentAuthController {

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if ("my-school".equals(username) && "awesome".equals(password)) {
            String token = SecurityUtil.genToken(username);
            return Map.of("token", token);
        }
        throw new RuntimeException("Invalid credentials");
    }

}
