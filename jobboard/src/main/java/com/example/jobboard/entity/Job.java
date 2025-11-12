package com.example.jobboard.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 5000, columnDefinition = "TEXT")
    private String description;

    @Column(length = 150)
    private String location;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double salary;
    
    @Column(name = "salary_min", columnDefinition = "DECIMAL(10,2)")
    private Double salaryMin;
    
    @Column(name = "salary_max", columnDefinition = "DECIMAL(10,2)")
    private Double salaryMax;
    
    @Column(name = "salary_currency", length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'USD'")
    private String salaryCurrency = "USD";

    // Status controlled by admin (recruiters create with default NEW or PENDING)
    @Column(length = 20, nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private String status; // e.g. PENDING, OPEN, CLOSED, FILLED
    
    // Job Details
    @Column(name = "job_type", length = 50)
    private String jobType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, TEMPORARY
    
    @Column(name = "experience_level", length = 50)
    private String experienceLevel; // ENTRY, MID, SENIOR, LEAD, EXECUTIVE
    
    @Column(name = "remote_type", length = 50)
    private String remoteType; // ON_SITE, REMOTE, HYBRID
    
    @Column(name = "education_level", length = 100)
    private String educationLevel; // HIGH_SCHOOL, BACHELOR, MASTER, PHD, etc.
    
    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills; // Comma-separated or JSON
    
    @Column(name = "preferred_skills", columnDefinition = "TEXT")
    private String preferredSkills;
    
    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;
    
    @Column(name = "company_name", length = 200)
    private String companyName;
    
    @Column(name = "company_website", length = 300)
    private String companyWebsite;
    
    @Column(name = "application_deadline")
    private java.time.LocalDate applicationDeadline;
    
    @Column(name = "positions_available")
    private Integer positionsAvailable = 1;
    
    @Column(name = "views_count", columnDefinition = "INT DEFAULT 0")
    private Integer viewsCount = 0;
    
    @Column(name = "applications_count", columnDefinition = "INT DEFAULT 0")
    private Integer applicationsCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by_id", nullable = false)
    private User postedBy;
    
    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }

    // No-args constructor
    public Job() {
    }

    // All-args constructor
    public Job(Long id, String title, String description, String location, Double salary, String status, User postedBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.salary = salary;
        this.status = status;
        this.postedBy = postedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    // New Fields Getters and Setters
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

    public java.time.LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(java.time.LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public Integer getPositionsAvailable() {
        return positionsAvailable;
    }

    public void setPositionsAvailable(Integer positionsAvailable) {
        this.positionsAvailable = positionsAvailable;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Integer getApplicationsCount() {
        return applicationsCount;
    }

    public void setApplicationsCount(Integer applicationsCount) {
        this.applicationsCount = applicationsCount;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
