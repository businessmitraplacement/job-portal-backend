package com.decoder.model;


import com.decoder.model.Job;
import com.decoder.model.Recruiter;
import java.time.LocalDateTime;

public class JobResponseDTO {

    private Long id;
    private String title;
    private String location;
    private String level;
    private String salary;
    private String description;
    private LocalDateTime date;

    private CompanyDTO companyId;

    public JobResponseDTO(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.location = job.getLocation();
        this.level = job.getLevel();
        this.salary = String.valueOf(job.getSalary());
        this.description = job.getDescription();
        this.date = job.getDate();
        this.companyId = new CompanyDTO(job.getCompanyId());
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public CompanyDTO getCompanyId() {
        return companyId;
    }

    public void setCompanyId(CompanyDTO companyId) {
        this.companyId = companyId;
    }

    // Inner DTO class for Recruiter
    public static class CompanyDTO {
        private Long id;
        private String name;
        private String image;

        public CompanyDTO(Recruiter recruiter) {
            this.id = recruiter.getId();
            this.name = recruiter.getName();
            this.image = recruiter.getImage();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
