// Placeholder for file in jobboard project
package com.example.jobboard.repository;

import com.example.jobboard.entity.Job;
import com.example.jobboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(String status);
    List<Job> findByPostedBy(User user);
}
