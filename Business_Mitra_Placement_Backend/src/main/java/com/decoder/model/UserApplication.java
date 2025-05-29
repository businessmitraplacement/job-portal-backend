package com.decoder.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class UserApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

//    private Long jobId;
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;


    private LocalDate appliedDate;

    private String status;
    


    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for userId
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getter and Setter for jobId
//    public Long getJobId() {
//        return jobId;
//    }
//
//    public void setJobId(Long jobId) {
//        this.jobId = jobId;
//    }
    
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }


    // Getter and Setter for appliedDate
    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
