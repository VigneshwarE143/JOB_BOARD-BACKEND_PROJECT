package com.example.jobboard.repository;

import com.example.jobboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String token);
    Optional<User> findByResetToken(String token);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
// Placeholder for file in jobboard project
