// Placeholder for file in jobboard project
package com.example.jobboard.service;

import com.example.jobboard.dto.JobRequest;
import com.example.jobboard.entity.Job;
import com.example.jobboard.entity.Role;
import com.example.jobboard.entity.User;
import com.example.jobboard.repository.JobRepository;
import com.example.jobboard.repository.UserRepository;
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

    /** Return all jobs with status OPEN */
    public List<Job> listOpenJobs() {
        return jobRepository.findByStatus("OPEN");
    }

    /** Create new job for recruiter/admin */
    public Job createJob(JobRequest req, String username) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = new Job();
        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setLocation(req.getLocation());
        job.setSalary(req.getSalary());
        job.setPostedBy(u);
        job.setStatus("PENDING"); // recruiters default to pending
        return jobRepository.save(job);
    }

    /** Update job (only owner or admin can update) */
    public Job updateJob(Long jobId, JobRequest req, String username) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ROLE_RECRUITER
                && !job.getPostedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed to update this job");
        }

        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setLocation(req.getLocation());
        job.setSalary(req.getSalary());
        return jobRepository.save(job);
    }

    /** Admin sets status */
    public Job setJobStatus(Long jobId, String status) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        return jobRepository.save(job);
    }
}
