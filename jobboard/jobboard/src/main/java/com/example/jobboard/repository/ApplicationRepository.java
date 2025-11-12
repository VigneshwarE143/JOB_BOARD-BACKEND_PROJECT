package com.example.jobboard.repository;

import com.example.jobboard.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findBySeekerId(Long seekerId);
    List<Application> findByJobId(Long jobId);
}
// Placeholder for file in jobboard project
