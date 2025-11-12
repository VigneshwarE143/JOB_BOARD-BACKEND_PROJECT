package com.example.jobboard.service;

import com.example.jobboard.dto.ApplicationRequest;
import com.example.jobboard.entity.Application;
import com.example.jobboard.entity.Job;
import com.example.jobboard.entity.Role;
import com.example.jobboard.entity.User;
import com.example.jobboard.exception.ResourceNotFoundException;
import com.example.jobboard.repository.ApplicationRepository;
import com.example.jobboard.repository.JobRepository;
import com.example.jobboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Apply for a job (SEEKER only)
     */
    public Application applyForJob(ApplicationRequest request, String username) {
        User seeker = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Only SEEKER can apply
        if (seeker.getRole() != Role.ROLE_SEEKER) {
            throw new AccessDeniedException("Only job seekers can apply for jobs");
        }

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + request.getJobId()));

        // Check if already applied
        List<Application> existingApplications = applicationRepository.findBySeekerId(seeker.getId());
        boolean alreadyApplied = existingApplications.stream()
                .anyMatch(app -> app.getJob().getId().equals(job.getId()));

        if (alreadyApplied) {
            throw new IllegalStateException("You have already applied for this job");
        }

        Application application = new Application();
        application.setJob(job);
        application.setSeeker(seeker);
        application.setCoverLetter(request.getCoverLetter());
        application.setResumeUrl(request.getResumeUrl());
        
        // Set additional candidate details
        application.setExpectedSalary(request.getExpectedSalary());
        application.setAvailableFrom(request.getAvailableFrom());
        application.setYearsOfExperience(request.getYearsOfExperience());
        application.setCurrentCompany(request.getCurrentCompany());
        application.setCurrentJobTitle(request.getCurrentJobTitle());
        application.setPortfolioUrl(request.getPortfolioUrl());
        application.setGithubUrl(request.getGithubUrl());
        application.setLinkedinUrl(request.getLinkedinUrl());
        application.setSkills(request.getSkills());

        return applicationRepository.save(application);
    }

    /**
     * Get applications based on user role:
     * - SEEKER: Only their own applications
     * - RECRUITER: Applications for jobs they posted
     * - ADMIN: All applications
     */
    public List<Application> getApplications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        switch (user.getRole()) {
            case ROLE_SEEKER:
                // Seekers see only their own applications
                return applicationRepository.findBySeekerId(user.getId());

            case ROLE_RECRUITER:
                // Recruiters see applications for their jobs
                List<Job> recruiterJobs = jobRepository.findByPostedById(user.getId());
                return applicationRepository.findAll().stream()
                        .filter(app -> recruiterJobs.stream()
                                .anyMatch(job -> job.getId().equals(app.getJob().getId())))
                        .toList();

            case ROLE_ADMIN:
                // Admins see all applications
                return applicationRepository.findAll();

            default:
                throw new AccessDeniedException("Invalid role");
        }
    }

    /**
     * Get applications for a specific job (RECRUITER/ADMIN only)
     */
    public List<Application> getApplicationsByJobId(Long jobId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        // Only job owner or admin can see applications for a job
        if (user.getRole() == Role.ROLE_RECRUITER && !job.getPostedBy().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only view applications for your own jobs");
        }

        if (user.getRole() == Role.ROLE_SEEKER) {
            throw new AccessDeniedException("Seekers cannot view all applications for a job");
        }

        return applicationRepository.findByJobId(jobId);
    }

    /**
     * Get application by ID (with permission checks)
     */
    public Application getApplicationById(Long id, String username) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Permission check
        boolean hasPermission = false;

        switch (user.getRole()) {
            case ROLE_SEEKER:
                // Seeker can only view their own application
                hasPermission = application.getSeeker().getId().equals(user.getId());
                break;

            case ROLE_RECRUITER:
                // Recruiter can view applications for their jobs
                hasPermission = application.getJob().getPostedBy().getId().equals(user.getId());
                break;

            case ROLE_ADMIN:
                // Admin can view any application
                hasPermission = true;
                break;
        }

        if (!hasPermission) {
            throw new AccessDeniedException("You do not have permission to view this application");
        }

        return application;
    }

    /**
     * Delete application (SEEKER can delete their own, ADMIN can delete any)
     */
    public void deleteApplication(Long id, String username) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Permission check
        boolean canDelete = false;

        if (user.getRole() == Role.ROLE_SEEKER && application.getSeeker().getId().equals(user.getId())) {
            canDelete = true; // Seeker can delete their own application
        } else if (user.getRole() == Role.ROLE_ADMIN) {
            canDelete = true; // Admin can delete any application
        }

        if (!canDelete) {
            throw new AccessDeniedException("You do not have permission to delete this application");
        }

        applicationRepository.deleteById(id);
    }
}
