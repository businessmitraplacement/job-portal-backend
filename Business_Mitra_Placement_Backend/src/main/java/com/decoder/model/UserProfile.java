package com.decoder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    private String name;
    private String gender;
    private String dob;
    private String location;
    private String phone;
    private String email;
    
    private String profileImage;

    
 // Getters and Setters
    
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
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
       
    public String getGender() {
    	return gender;
    }
    
    public void setGender(String gender) {
    	this.gender = gender;
    }
    
    public String getDob() {
    	return dob;
    }
    
    public void setDob(String dob) {
    	this.dob = dob;
    }
    
    public String getLocation() {
    	return location;
    }
    
    public void setLocation(String location) {
    	this.location = location;
    }
    
    public String getPhone() {
    	return phone;
    }
    
    public void setPhone(String phone) {
    	this.phone = phone;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

