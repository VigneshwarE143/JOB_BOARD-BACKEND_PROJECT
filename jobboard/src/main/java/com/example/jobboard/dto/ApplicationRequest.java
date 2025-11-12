package com.example.jobboard.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class ApplicationRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    private String coverLetter;
    private String resumeUrl; // optional
    
    // Additional candidate details
    @Positive(message = "Expected salary must be positive")
    private Double expectedSalary;
    
    private LocalDate availableFrom;
    
    @Positive(message = "Years of experience must be positive")
    private Integer yearsOfExperience;
    
    private String currentCompany;
    private String currentJobTitle;
    
    // Social/Portfolio links
    private String portfolioUrl;
    private String githubUrl;
    private String linkedinUrl;
    
    // Skills (comma-separated or JSON)
    private String skills;

    // No-args constructor
    public ApplicationRequest() {}

    // All-args constructor
    public ApplicationRequest(Long jobId, String coverLetter, String resumeUrl) {
        this.jobId = jobId;
        this.coverLetter = coverLetter;
        this.resumeUrl = resumeUrl;
    }

    // Getters and setters
    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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

    public Double getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(Double expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalDate availableFrom) {
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
}
