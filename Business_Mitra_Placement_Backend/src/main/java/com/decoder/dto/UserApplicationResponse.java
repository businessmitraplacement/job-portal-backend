package com.decoder.dto;

import java.time.LocalDate;

public class UserApplicationResponse {

    private String name;
    private String jobTitle;
    private String location;
    private LocalDate appliedDate;
    private String status;

    // No-argument constructor
    public UserApplicationResponse() {
    }

    // All-argument constructor
    public UserApplicationResponse(String name, String jobTitle, String location, LocalDate appliedDate, String status) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.location = location;
        this.appliedDate = appliedDate;
        this.status = status;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
