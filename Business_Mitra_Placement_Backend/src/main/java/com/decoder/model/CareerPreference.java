package com.decoder.model;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "career_preferences")
public class CareerPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    private String jobType;
    private String availability;

    // Store as comma-separated string
    @Column(length = 500)
    private String locations;  

    // Getters and setters
    
    public Long getId() {
    	return id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }
    
    public User getUser() {
    	return user;
    }
    
    public void setUser(User user) {
    	this.user = user;
    }
    
    public List<String> getJobTypeList() {
        return jobType != null ? Arrays.asList(jobType.split(",")) : null;
    }
    public void setJobTypeList(List<String> jobTypeList) {
        this.jobType = jobTypeList != null ? String.join(",", jobTypeList) : null;
    }
    
    public String getAvailability() {
    	return availability;
    }
    
    public void setAvailability(String availability) {
    	this.availability = availability;
    }
    
    // Helper to handle List in DTO
    public List<String> getLocationList() {
        return locations != null ? Arrays.asList(locations.split(",")) : null;
    }

    public void setLocationList(List<String> locationList) {
        this.locations = locationList != null ? String.join(",", locationList) : null;
    }

}

