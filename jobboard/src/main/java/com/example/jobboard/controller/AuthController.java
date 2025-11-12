package com.example.jobboard.controller;

import com.example.jobboard.dto.*;
import com.example.jobboard.entity.Role;
import com.example.jobboard.entity.User;
import com.example.jobboard.repository.UserRepository;
import com.example.jobboard.service.EmailService;
import com.example.jobboard.config.JwtTokenProvider;
import com.example.jobboard.exception.ResourceNotFoundException;
import com.example.jobboard.exception.InvalidTokenException;
import com.example.jobboard.exception.AccountLockedException;
import jakarta.validation.Valid;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest req) {
        // Check username uniqueness
        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username already exists"));
        }

        // Check email uniqueness
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already registered"));
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        // Set role
        if (req.getRole() != null && !req.getRole().isBlank()) {
            try {
                u.setRole(Role.valueOf(req.getRole().startsWith("ROLE_") ?
                                       req.getRole() : "ROLE_" + req.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid role: " + req.getRole()));
            }
        } else {
            u.setRole(Role.ROLE_SEEKER);
        }

        // Email verification setup
        u.setEnabled(false); // Account disabled until email verified
        u.setEmailVerified(false);
        u.setVerificationToken(UUID.randomUUID().toString());

        userRepository.save(u);

        // Send verification email
        emailService.sendVerificationEmail(u.getEmail(), u.getVerificationToken());

        return ResponseEntity.ok(new MessageResponse(
            "Registration successful! Please check your email to verify your account."
        ));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid verification token"));

        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Email verified successfully! You can now login."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        // Check if account is locked
        if (user.getLockTime() != null && user.getLockTime().isAfter(LocalDateTime.now())) {
            long minutesLeft = java.time.Duration.between(LocalDateTime.now(), user.getLockTime()).toMinutes();
            throw new AccountLockedException(
                "Account is locked due to multiple failed attempts. Try again in " + minutesLeft + " minutes."
            );
        }

        // Reset lock if time expired
        if (user.getLockTime() != null && user.getLockTime().isBefore(LocalDateTime.now())) {
            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

            // Reset failed attempts on successful login
            if (user.getFailedAttempts() > 0) {
                user.setFailedAttempts(0);
                user.setLockTime(null);
                userRepository.save(user);
            }

            String token = jwtTokenProvider.generateToken(auth);

            return ResponseEntity.ok(new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
            ));
        } catch (BadCredentialsException e) {
            // Increment failed attempts
            user.setFailedAttempts(user.getFailedAttempts() + 1);

            if (user.getFailedAttempts() >= 5) {
                user.setLockTime(LocalDateTime.now().plusMinutes(30));
                userRepository.save(user);
                throw new AccountLockedException(
                    "Account locked due to 5 failed login attempts. Try again in 30 minutes."
                );
            }

            userRepository.save(user);
            throw new BadCredentialsException(
                "Invalid credentials. Attempts remaining: " + (5 - user.getFailedAttempts())
            );
        } catch (DisabledException e) {
            throw new DisabledException("Account is disabled. Please verify your email.");
        } catch (LockedException e) {
            throw new LockedException("Account is locked");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        // Send password reset email
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        return ResponseEntity.ok(new MessageResponse("Password reset link sent to your email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
        User user = userRepository.findByResetToken(req.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Password reset successful! You can now login."));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest req,
                                            Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Current password is incorrect"));
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserDto userDto = new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name(),
            user.isEnabled(),
            user.isEmailVerified()
        );

        return ResponseEntity.ok(userDto);
    }
}