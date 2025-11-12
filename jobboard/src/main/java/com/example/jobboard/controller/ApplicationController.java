package com.example.jobboard.controller;

import com.example.jobboard.dto.ApplicationRequest;
import com.example.jobboard.dto.MessageResponse;
import com.example.jobboard.entity.Application;
import com.example.jobboard.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * Apply for a job (SEEKER only)
     * POST /api/applications
     */
    @PostMapping
    @PreAuthorize("hasRole('SEEKER')")
    public ResponseEntity<?> applyForJob(
            @Valid @RequestBody ApplicationRequest request,
            Authentication authentication) {
        try {
            Application application = applicationService.applyForJob(request, authentication.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(application);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Get applications based on user role:
     * - SEEKER: Only their own applications
     * - RECRUITER: Applications for jobs they posted
     * - ADMIN: All applications
     * GET /api/applications
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SEEKER', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<List<Application>> getApplications(Authentication authentication) {
        List<Application> applications = applicationService.getApplications(authentication.getName());
        return ResponseEntity.ok(applications);
    }

    /**
     * Get applications for a specific job (RECRUITER/ADMIN only)
     * GET /api/applications/job/{jobId}
     */
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<List<Application>> getApplicationsByJobId(
            @PathVariable Long jobId,
            Authentication authentication) {
        List<Application> applications = applicationService.getApplicationsByJobId(jobId, authentication.getName());
        return ResponseEntity.ok(applications);
    }

    /**
     * Get application by ID (with permission checks)
     * GET /api/applications/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SEEKER', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<Application> getApplicationById(
            @PathVariable Long id,
            Authentication authentication) {
        Application application = applicationService.getApplicationById(id, authentication.getName());
        return ResponseEntity.ok(application);
    }

    /**
     * Delete application (SEEKER can delete their own, ADMIN can delete any)
     * DELETE /api/applications/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SEEKER', 'ADMIN')")
    public ResponseEntity<MessageResponse> deleteApplication(
            @PathVariable Long id,
            Authentication authentication) {
        applicationService.deleteApplication(id, authentication.getName());
        return ResponseEntity.ok(new MessageResponse("Application deleted successfully"));
    }
}
