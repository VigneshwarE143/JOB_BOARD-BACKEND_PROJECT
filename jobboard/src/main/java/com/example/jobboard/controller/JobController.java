package com.example.jobboard.controller;

import com.example.jobboard.dto.JobRequest;
import com.example.jobboard.dto.JobDto;
import com.example.jobboard.entity.Job;
import com.example.jobboard.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * List all OPEN jobs - PUBLIC
     */
    @GetMapping
    public List<JobDto> listOpenJobs() {
        return jobService.listOpenJobs().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * List all jobs (regardless of status) - ADMIN only
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<JobDto> listAllJobs() {
        return jobService.listAllJobs().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get job by ID - PUBLIC (increments view count)
     */
    @GetMapping("/{id}")
    public JobDto getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        return toDto(job);
    }
    
    /**
     * Get my posted jobs - RECRUITER/ADMIN only
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public List<JobDto> getMyJobs(Authentication auth) {
        return jobService.getMyJobs(auth.getName()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Create job - RECRUITER/ADMIN
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<JobDto> createJob(@Valid @RequestBody JobRequest req, Authentication auth) {
        Job created = jobService.createJob(req, auth.getName());
        return ResponseEntity.ok(toDto(created));
    }

    /**
     * Update job - Only owner or ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<JobDto> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest req, Authentication auth) {
        Job updated = jobService.updateJob(id, req, auth.getName());
        return ResponseEntity.ok(toDto(updated));
    }

    /**
     * Set job status - ADMIN only
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDto> setJobStatus(@PathVariable Long id, @RequestBody JobStatusRequest req, Authentication auth) {
        Job updated = jobService.setJobStatus(id, req.getStatus(), auth.getName());
        return ResponseEntity.ok(toDto(updated));
    }
    
    /**
     * Delete job - Only owner or ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id, Authentication auth) {
        jobService.deleteJob(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    /**
     * Map Job entity to DTO
     */
    private JobDto toDto(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setStatus(job.getStatus());
        dto.setPostedBy(job.getPostedBy() != null ? job.getPostedBy().getUsername() : null);
        
        // Add new fields
        dto.setSalaryMin(job.getSalaryMin());
        dto.setSalaryMax(job.getSalaryMax());
        dto.setSalaryCurrency(job.getSalaryCurrency());
        dto.setJobType(job.getJobType());
        dto.setExperienceLevel(job.getExperienceLevel());
        dto.setRemoteType(job.getRemoteType());
        dto.setEducationLevel(job.getEducationLevel());
        dto.setRequiredSkills(job.getRequiredSkills());
        dto.setPreferredSkills(job.getPreferredSkills());
        dto.setBenefits(job.getBenefits());
        dto.setCompanyName(job.getCompanyName());
        dto.setCompanyWebsite(job.getCompanyWebsite());
        dto.setApplicationDeadline(job.getApplicationDeadline());
        dto.setPositionsAvailable(job.getPositionsAvailable());
        dto.setViewsCount(job.getViewsCount());
        dto.setApplicationsCount(job.getApplicationsCount());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setUpdatedAt(job.getUpdatedAt());
        
        return dto;
    }

    // ----------------------------
    // DTO for job status update
    // ----------------------------
    public static class JobStatusRequest {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
