package com.example.jobboard.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class JobRequest {

    @NotBlank(message = "Job title is required")
    private String title;
    
    @NotBlank(message = "Job description is required")
    private String description;
    
    private String location;
    
    private Double salary; // Legacy field
    
    // Salary range fields
    private Double salaryMin;
    private Double salaryMax;
    private String salaryCurrency; // e.g., "USD", "EUR", "INR"
    
    // Job details
    private String jobType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP
    private String experienceLevel; // ENTRY_LEVEL, JUNIOR, MID_LEVEL, SENIOR, LEAD
    private String remoteType; // ON_SITE, REMOTE, HYBRID
    private String educationLevel; // HIGH_SCHOOL, BACHELORS, MASTERS, PHD
    
    // Skills
    private String requiredSkills; // Comma-separated or JSON
    private String preferredSkills; // Comma-separated or JSON
    
    // Additional info
    private String benefits; // TEXT
    private String companyName;
    private String companyWebsite;
    
    // Application details
    private LocalDate applicationDeadline;
    
    @Min(value = 1, message = "Positions available must be at least 1")
    private Integer positionsAvailable;
    
    // Status (only admin can set via update)
    private String status; // PENDING, OPEN, CLOSED, FILLED

    // No-args constructor
    public JobRequest() {
    }

    // Legacy constructor for backward compatibility
    public JobRequest(String title, String description, String location, Double salary) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.salary = salary;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Double salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Double getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Double salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getRemoteType() {
        return remoteType;
    }

    public void setRemoteType(String remoteType) {
        this.remoteType = remoteType;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getPreferredSkills() {
        return preferredSkills;
    }

    public void setPreferredSkills(String preferredSkills) {
        this.preferredSkills = preferredSkills;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public Integer getPositionsAvailable() {
        return positionsAvailable;
    }

    public void setPositionsAvailable(Integer positionsAvailable) {
        this.positionsAvailable = positionsAvailable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
