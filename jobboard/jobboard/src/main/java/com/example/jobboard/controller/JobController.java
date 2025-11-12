package com.example.jobboard.controller;

import com.example.jobboard.dto.JobRequest;
import com.example.jobboard.dto.ApplicationRequest;
import com.example.jobboard.dto.JobDto;
import com.example.jobboard.entity.Application;
import com.example.jobboard.entity.Job;
import com.example.jobboard.entity.Role;
import com.example.jobboard.entity.User;
import com.example.jobboard.repository.ApplicationRepository;
import com.example.jobboard.repository.JobRepository;
import com.example.jobboard.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public JobController(JobRepository jobRepository,
                         UserRepository userRepository,
                         ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    // ----------------------------
    // List all OPEN jobs (public)
    // ----------------------------
    @GetMapping
    public List<JobDto> listJobs() {
        return jobRepository.findByStatus("APPROVED").stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ----------------------------
    // Create job (Recruiter/Admin)
    // ----------------------------
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')") // Spring adds ROLE_ prefix automatically
    public ResponseEntity<?> createJob(@RequestBody JobRequest req, Authentication auth) {
        User u = userRepository.findByUsername(auth.getName()).orElseThrow();
        Job job = new Job();
        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setLocation(req.getLocation());
        job.setSalary(req.getSalary());
        job.setPostedBy(u);
        job.setStatus("PENDING"); // recruiter job default pending
        Job saved = jobRepository.save(job);
        return ResponseEntity.ok(toDto(saved));
    }

    // ----------------------------
    // Update job (Recruiter/Admin)
    // ----------------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobRequest req, Authentication auth) {
        Job job = jobRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();

        if (user.getRole() == Role.ROLE_RECRUITER && !job.getPostedBy().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Not allowed");
        }
        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setLocation(req.getLocation());
        job.setSalary(req.getSalary());
        Job saved = jobRepository.save(job);
        return ResponseEntity.ok(toDto(saved));
    }

    // ----------------------------
    // Admin-only: set job status
    // ----------------------------
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setStatus(@PathVariable Long id, @RequestBody JobStatusRequest req) {
        Job job = jobRepository.findById(id).orElseThrow();
        job.setStatus(req.getStatus());
        return ResponseEntity.ok(toDto(jobRepository.save(job)));
    }

    // ----------------------------
    // Job Seeker: apply to job
    // ----------------------------
    @PostMapping("/{id}/apply")
    @PreAuthorize("hasRole('SEEKER')")
    public ResponseEntity<?> applyToJob(@PathVariable Long id, @RequestBody ApplicationRequest req, Authentication auth) {
        Job job = jobRepository.findById(id).orElseThrow();
        User seeker = userRepository.findByUsername(auth.getName()).orElseThrow();

        Application application = new Application();
        application.setJob(job);
        application.setSeeker(seeker);
        application.setCoverLetter(req.getCoverLetter());
        application.setResumeUrl(req.getResumeUrl());

        Application saved = applicationRepository.save(application);
        return ResponseEntity.ok(saved); // Application DTO optional here
    }

    // helper to map entity -> DTO
    private JobDto toDto(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setStatus(job.getStatus());
        dto.setPostedBy(job.getPostedBy() != null ? job.getPostedBy().getUsername() : null);
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
