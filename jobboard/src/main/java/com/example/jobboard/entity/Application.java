package com.example.jobboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seeker_id", nullable = false)
    private User seeker;

    @Column(length = 5000, columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl; // optional
    
    // Application Status and Tracking
    @Column(name = "status", length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'SUBMITTED'")
    private String status; // SUBMITTED, UNDER_REVIEW, SHORTLISTED, INTERVIEW_SCHEDULED, REJECTED, ACCEPTED, WITHDRAWN
    
    @Column(name = "expected_salary", columnDefinition = "DECIMAL(10,2)")
    private Double expectedSalary;
    
    @Column(name = "available_from")
    private java.time.LocalDate availableFrom;
    
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    
    @Column(name = "current_company", length = 200)
    private String currentCompany;
    
    @Column(name = "current_job_title", length = 150)
    private String currentJobTitle;
    
    @Column(name = "portfolio_url", length = 500)
    private String portfolioUrl;
    
    @Column(name = "github_url", length = 500)
    private String githubUrl;
    
    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;
    
    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills; // Comma-separated skills
    
    @Column(name = "recruiter_notes", columnDefinition = "TEXT")
    private String recruiterNotes; // Private notes from recruiter
    
    @Column(name = "applied_at")
    private java.time.LocalDateTime appliedAt;
    
    @Column(name = "reviewed_at")
    private java.time.LocalDateTime reviewedAt;
    
    @Column(name = "reviewed_by_id")
    private Long reviewedBy; // User ID of recruiter who reviewed
    
    @Column(name = "interview_date")
    private java.time.LocalDateTime interviewDate;
    
    @Column(name = "withdrawal_reason", columnDefinition = "TEXT")
    private String withdrawalReason;

    @PrePersist
    protected void onCreate() {
        appliedAt = java.time.LocalDateTime.now();
        if (status == null) {
            status = "SUBMITTED";
        }
    }

    // No-args constructor
    public Application() {
    }

    // All-args constructor
    public Application(Long id, Job job, User seeker, String coverLetter, String resumeUrl) {
        this.id = id;
        this.job = job;
        this.seeker = seeker;
        this.coverLetter = coverLetter;
        this.resumeUrl = resumeUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getSeeker() {
        return seeker;
    }

    public void setSeeker(User seeker) {
        this.seeker = seeker;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    // New Fields Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(Double expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public java.time.LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(java.time.LocalDate availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    public String getCurrentJobTitle() {
        return currentJobTitle;
    }

    public void setCurrentJobTitle(String currentJobTitle) {
        this.currentJobTitle = currentJobTitle;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getRecruiterNotes() {
        return recruiterNotes;
    }

    public void setRecruiterNotes(String recruiterNotes) {
        this.recruiterNotes = recruiterNotes;
    }

    public java.time.LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public java.time.LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(java.time.LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Long getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Long reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public java.time.LocalDateTime getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(java.time.LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    public void setWithdrawalReason(String withdrawalReason) {
        this.withdrawalReason = withdrawalReason;
    }
}
