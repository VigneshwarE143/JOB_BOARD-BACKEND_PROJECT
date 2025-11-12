package com.example.jobboard.controller;

import com.example.jobboard.dto.AuthRequest;
import com.example.jobboard.entity.Role;
import com.example.jobboard.entity.User;
import com.example.jobboard.repository.UserRepository;
import com.example.jobboard.config.JwtTokenProvider;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body("username exists");
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        // ✅ if client provided a role, use it, else default to SEEKER
        if (req.getRole() != null && !req.getRole().isBlank()) {
            try {
                // convert string to enum
                u.setRole(Role.valueOf(req.getRole().startsWith("ROLE_") ?
                                       req.getRole() : "ROLE_" + req.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid role: " + req.getRole());
            }
        } else {
            u.setRole(Role.ROLE_SEEKER);
        }

        userRepository.save(u);
        return ResponseEntity.ok("registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        String token = jwtTokenProvider.generateToken(auth);
        return ResponseEntity.ok(token);
    }
}
// Placeholder for file in jobboard project
