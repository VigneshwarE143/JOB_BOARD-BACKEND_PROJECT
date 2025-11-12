package com.example.jobboard.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    /**
     * Send verification email to user
     * TODO: Implement actual email sending using JavaMailSender
     */
    public void sendVerificationEmail(String email, String token) {
        // For now, just log it
        System.out.println("==============================================");
        System.out.println("VERIFICATION EMAIL");
        System.out.println("To: " + email);
        System.out.println("Verification Link: http://localhost:8080/api/auth/verify-email?token=" + token);
        System.out.println("==============================================");
        
        // TODO: Implement actual email sending
        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setTo(email);
        // message.setSubject("Email Verification");
        // message.setText("Click the link to verify: http://localhost:8080/api/auth/verify-email?token=" + token);
        // mailSender.send(message);
    }
    
    /**
     * Send password reset email
     * TODO: Implement actual email sending using JavaMailSender
     */
    public void sendPasswordResetEmail(String email, String token) {
        System.out.println("==============================================");
        System.out.println("PASSWORD RESET EMAIL");
        System.out.println("To: " + email);
        System.out.println("Reset Link: http://localhost:8080/api/auth/reset-password?token=" + token);
        System.out.println("==============================================");
        
        // TODO: Implement actual email sending
        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setTo(email);
        // message.setSubject("Password Reset");
        // message.setText("Click the link to reset password: http://localhost:8080/api/auth/reset-password?token=" + token);
        // mailSender.send(message);
    }
}
