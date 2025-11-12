-- ============================================
-- JOB BOARD DATABASE SETUP FOR MYSQL
-- ============================================
-- This script creates the database and tables
-- You can run this manually or let Spring JPA auto-create tables
-- ============================================

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS job_house 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE job_house;

-- Drop existing tables (if you want a fresh start)
-- DROP TABLE IF EXISTS job_applications;
-- DROP TABLE IF EXISTS jobs;
-- DROP TABLE IF EXISTS users;

-- ============================================
-- USERS TABLE (with authentication features)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE NOT NULL,
    verification_token VARCHAR(255),
    reset_token VARCHAR(255),
    reset_token_expiry DATETIME,
    failed_attempts INT DEFAULT 0 NOT NULL,
    lock_time DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_verification_token (verification_token),
    INDEX idx_reset_token (reset_token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- JOBS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(150),
    salary DECIMAL(10, 2),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    posted_by_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (posted_by_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_status (status),
    INDEX idx_posted_by (posted_by_id),
    INDEX idx_location (location)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- JOB APPLICATIONS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS job_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    seeker_id BIGINT NOT NULL,
    cover_letter TEXT,
    resume_url VARCHAR(500),
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (seeker_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_application (job_id, seeker_id),
    INDEX idx_job_id (job_id),
    INDEX idx_seeker_id (seeker_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- SAMPLE DATA (Optional - for testing)
-- ============================================

-- Insert test admin user (password: admin123)
-- Password is BCrypt encoded
INSERT IGNORE INTO users (username, email, password, role, enabled, email_verified) VALUES
('admin', 'admin@jobboard.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', TRUE, TRUE);

-- Insert test recruiter (password: recruiter123)
INSERT IGNORE INTO users (username, email, password, role, enabled, email_verified) VALUES
('recruiter1', 'recruiter@company.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RECRUITER', TRUE, TRUE);

-- Insert test job seeker (password: seeker123)
INSERT IGNORE INTO users (username, email, password, role, enabled, email_verified) VALUES
('seeker1', 'seeker@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SEEKER', TRUE, TRUE);

-- ============================================
-- USEFUL QUERIES
-- ============================================

-- View all users
-- SELECT id, username, email, role, enabled, email_verified FROM users;

-- View all jobs
-- SELECT j.id, j.title, j.location, j.salary, j.status, u.username as posted_by 
-- FROM jobs j JOIN users u ON j.posted_by_id = u.id;

-- View all applications
-- SELECT a.id, j.title as job_title, u.username as applicant, a.applied_at 
-- FROM job_applications a 
-- JOIN jobs j ON a.job_id = j.id 
-- JOIN users u ON a.seeker_id = u.id;

-- Check database size
-- SELECT 
--     table_schema AS 'Database',
--     ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
-- FROM information_schema.TABLES 
-- WHERE table_schema = 'job_house'
-- GROUP BY table_schema;
