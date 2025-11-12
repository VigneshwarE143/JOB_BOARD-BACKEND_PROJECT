package com.example.jobboard.dto;

public class ApplicationRequest {

    private String coverLetter;
    private String resumeUrl; // optional

    // No-args constructor
    public ApplicationRequest() {}

    // All-args constructor
    public ApplicationRequest(String coverLetter, String resumeUrl) {
        this.coverLetter = coverLetter;
        this.resumeUrl = resumeUrl;
    }

    // Getters and setters
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
