package com.example.jobboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobboardApplication.class, args);
    }
}

/** 
jobboard/
├── src/main/java/com/example/jobboard/
│   ├── JobboardApplication.java          ← App starts here
│   ├── config/                           ← Configuration files
│   │   ├── SecurityConfig.java           ← Security rules
│   │   ├── JwtAuthenticationFilter.java  ← Checks login tokens
│   │   └── JwtTokenProvider.java         ← Creates/validates tokens
│   ├── controller/                       ← API endpoints (routes)
│   │   ├── AuthController.java           ← /api/auth/login, /register
│   │   ├── JobController.java            ← /api/jobs (create, list, delete)
│   │   └── ApplicationController.java    ← /api/applications
│   ├── service/                          ← Business logic
│   │   ├── UserService.java              ← User-related operations
│   │   ├── JobService.java               ← Job-related operations
│   │   └── ApplicationService.java       ← Application logic
│   ├── repository/                       ← Database access
│   │   ├── UserRepository.java           ← CRUD for users
│   │   ├── JobRepository.java            ← CRUD for jobs
│   │   └── ApplicationRepository.java    ← CRUD for applications
│   ├── model/ (or entity/)              ← Database tables as Java classes
│   │   ├── User.java                     ← User table
│   │   ├── Job.java                      ← Job table
│   │   ├── Application.java              ← Application table
│   │   └── Role.java                     ← Roles (USER, RECRUITER, ADMIN)
│   └── security/
│       └── CustomUserDetailsService.java ← Loads user for login
└── src/main/resources/
    └── application.properties            ← Database URL, passwords, settings 
    */