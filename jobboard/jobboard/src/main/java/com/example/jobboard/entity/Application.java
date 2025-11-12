package com.example.jobboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Job job;

    @ManyToOne(optional = false)
    private User seeker;

    @Column(length = 4000)
    private String coverLetter;

    private String resumeUrl; // optional

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
}
