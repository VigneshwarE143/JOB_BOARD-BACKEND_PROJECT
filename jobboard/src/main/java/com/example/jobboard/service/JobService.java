// Placeholder for file in jobboard project
package com.example.jobboard.service;

import com.example.jobboard.dto.JobRequest;
import com.example.jobboard.entity.Job;
import com.example.jobboard.entity.Role;
import com.example.jobboard.entity.User;
import com.example.jobboard.exception.ResourceNotFoundException;
import com.example.jobboard.repository.JobRepository;
import com.example.jobboard.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    /** Return all jobs - PUBLIC (but filter sensitive data in controller) */
    public List<Job> listAllJobs() {
        return jobRepository.findAll();
    }

    /** Return all OPEN jobs - PUBLIC */
    public List<Job> listOpenJobs() {
        return jobRepository.findByStatus("OPEN");
    }
    
    /** Get job by ID - PUBLIC (increment view count) */
    public Job getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        
        // Increment view count
        job.setViewsCount(job.getViewsCount() + 1);
        jobRepository.save(job);
        
        return job;
    }
    
    /** Get jobs posted by specific recruiter (RECRUITER/ADMIN only) */
    public List<Job> getMyJobs(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getRole() != Role.ROLE_RECRUITER && user.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Only recruiters can view their posted jobs");
        }
        
        return jobRepository.findByPostedById(user.getId());
    }

    /** Create new job for recruiter/admin */
    public Job createJob(JobRequest req, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.ROLE_RECRUITER && user.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Only recruiters and admins can create jobs");
        }

        Job job = new Job();
        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setLocation(req.getLocation());
        job.setSalary(req.getSalary());
        job.setPostedBy(user);
        job.setStatus("PENDING"); // Recruiters default to PENDING, admin approval required
        
        // Set additional fields if provided
        if (req.getSalaryMin() != null) job.setSalaryMin(req.getSalaryMin());
        if (req.getSalaryMax() != null) job.setSalaryMax(req.getSalaryMax());
        if (req.getSalaryCurrency() != null) job.setSalaryCurrency(req.getSalaryCurrency());
        if (req.getJobType() != null) job.setJobType(req.getJobType());
        if (req.getExperienceLevel() != null) job.setExperienceLevel(req.getExperienceLevel());
        if (req.getRemoteType() != null) job.setRemoteType(req.getRemoteType());
        if (req.getEducationLevel() != null) job.setEducationLevel(req.getEducationLevel());
        if (req.getRequiredSkills() != null) job.setRequiredSkills(req.getRequiredSkills());
        if (req.getPreferredSkills() != null) job.setPreferredSkills(req.getPreferredSkills());
        if (req.getBenefits() != null) job.setBenefits(req.getBenefits());
        if (req.getCompanyName() != null) job.setCompanyName(req.getCompanyName());
        if (req.getCompanyWebsite() != null) job.setCompanyWebsite(req.getCompanyWebsite());
        if (req.getApplicationDeadline() != null) job.setApplicationDeadline(req.getApplicationDeadline());
        if (req.getPositionsAvailable() != null) job.setPositionsAvailable(req.getPositionsAvailable());
        
        return jobRepository.save(job);
    }

    /** Update job (only owner or admin can update) */
    public Job updateJob(Long jobId, JobRequest req, String username) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Permission check: Only job owner or admin can update
        if (user.getRole() == Role.ROLE_RECRUITER && !job.getPostedBy().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only update jobs that you posted");
        }
        
        if (user.getRole() == Role.ROLE_SEEKER) {
            throw new AccessDeniedException("Job seekers cannot update jobs");
        }

        // Update fields
        if (req.getTitle() != null) job.setTitle(req.getTitle());
        if (req.getDescription() != null) job.setDescription(req.getDescription());
        if (req.getLocation() != null) job.setLocation(req.getLocation());
        if (req.getSalary() != null) job.setSalary(req.getSalary());
        if (req.getSalaryMin() != null) job.setSalaryMin(req.getSalaryMin());
        if (req.getSalaryMax() != null) job.setSalaryMax(req.getSalaryMax());
        if (req.getSalaryCurrency() != null) job.setSalaryCurrency(req.getSalaryCurrency());
        if (req.getJobType() != null) job.setJobType(req.getJobType());
        if (req.getExperienceLevel() != null) job.setExperienceLevel(req.getExperienceLevel());
        if (req.getRemoteType() != null) job.setRemoteType(req.getRemoteType());
        if (req.getEducationLevel() != null) job.setEducationLevel(req.getEducationLevel());
        if (req.getRequiredSkills() != null) job.setRequiredSkills(req.getRequiredSkills());
        if (req.getPreferredSkills() != null) job.setPreferredSkills(req.getPreferredSkills());
        if (req.getBenefits() != null) job.setBenefits(req.getBenefits());
        if (req.getCompanyName() != null) job.setCompanyName(req.getCompanyName());
        if (req.getCompanyWebsite() != null) job.setCompanyWebsite(req.getCompanyWebsite());
        if (req.getApplicationDeadline() != null) job.setApplicationDeadline(req.getApplicationDeadline());
        if (req.getPositionsAvailable() != null) job.setPositionsAvailable(req.getPositionsAvailable());
        
        // Only admin can change status via update
        if (user.getRole() == Role.ROLE_ADMIN && req.getStatus() != null) {
            job.setStatus(req.getStatus());
        }
        
        return jobRepository.save(job);
    }

    /** Admin sets/changes job status */
    public Job setJobStatus(Long jobId, String status, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Only admins can change job status");
        }
        
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        
        job.setStatus(status);
        return jobRepository.save(job);
    }
    
    /** Delete job (only owner or admin) */
    public void deleteJob(Long jobId, String username) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Permission check
        if (user.getRole() == Role.ROLE_RECRUITER && !job.getPostedBy().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only delete jobs that you posted");
        }
        
        if (user.getRole() == Role.ROLE_SEEKER) {
            throw new AccessDeniedException("Job seekers cannot delete jobs");
        }

        jobRepository.deleteById(jobId);
    }
}

