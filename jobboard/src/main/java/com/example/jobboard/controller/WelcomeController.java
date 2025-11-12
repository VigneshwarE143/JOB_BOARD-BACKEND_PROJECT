package com.example.jobboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Job Board Backend API");
        response.put("version", "1.0.0");
        response.put("status", "Running");
        response.put("message", "Welcome! Use /api/auth endpoints to get started.");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("Register", "POST /api/auth/register");
        endpoints.put("Login", "POST /api/auth/login");
        endpoints.put("Verify Email", "GET /api/auth/verify?token=YOUR_TOKEN");
        endpoints.put("Forgot Password", "POST /api/auth/forgot-password");
        endpoints.put("Reset Password", "POST /api/auth/reset-password");
        endpoints.put("Change Password", "POST /api/auth/change-password");
        endpoints.put("Current User", "GET /api/auth/me");
        endpoints.put("List Jobs", "GET /api/jobs");
        endpoints.put("Job Details", "GET /api/jobs/{id}");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> docs = new HashMap<>();
        docs.put("Quick Start", "See QUICK_START.md for API examples");
        docs.put("Features", "See NEW_FEATURES.md for all features");
        docs.put("Authentication", "See AUTHENTICATION_FLOWS.md for auth workflows");
        
        response.put("documentation", docs);
        
        return response;
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("database", "MySQL Connected");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }
}
