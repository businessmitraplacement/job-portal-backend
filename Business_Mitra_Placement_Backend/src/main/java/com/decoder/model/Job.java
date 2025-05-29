package com.decoder.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.Boolean;
import jakarta.persistence.*;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;

    private String location;

    private String level;

    private Integer salary;
    
    private int applicants = 0;
    
    @Column(name = "visible")
    private Boolean visible = true;

 
    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER ensures recruiter data is fetched too
    @JoinColumn(name = "recruiter_id")  // FK column in job table
    private Recruiter companyId;
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserApplication> applications = new ArrayList<>();



    // --- Getters and Setters ---

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
    
    public int getApplicants() {
    	return applicants;
    }
    
    public void setApplicants(int applications) {
    	this.applicants = applicants;
    }
    
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Recruiter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Recruiter companyId) {
        this.companyId = companyId;
    }
    
    public List<UserApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<UserApplication> applications) {
        this.applications = applications;
    }
    
    public void addApplication(UserApplication app) {
        applications.add(app);
        app.setJob(this);
    }

    public void removeApplication(UserApplication app) {
        applications.remove(app);
        app.setJob(null);
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

}
