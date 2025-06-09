package com.decoder.dto;

import java.util.List;

public class CareerPreferenceDTO {
    private List<String> jobType;
    private String availability;
    private List<String> locations;

    // Getters and Setters
    
    public List<String> getJobType() {
    	return jobType;
    }
    public void setJobType(List<String> jobType) {
    	this.jobType = jobType;
    }
    
    public String getAvailability() {
    	return availability;
    }
    
    public void setAvailability(String availability) {
    	this.availability = availability;
    }
    
    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
