package com.jobtracker.dto;

import java.time.LocalDate;

public class JobApplicationResponse {

    private Long id;
    private String companyName;
    private String jobRole;
    private LocalDate applicationDate;
    private String status;
    private String jobLink;
    private String notes;

    public JobApplicationResponse(
            Long id,
            String companyName,
            String jobRole,
            LocalDate applicationDate,
            String status,
            String jobLink,
            String notes) {

        this.id = id;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.applicationDate = applicationDate;
        this.status = status;
        this.jobLink = jobLink;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public String getJobLink() {
        return jobLink;
    }

    public String getNotes() {
        return notes;
    }
}