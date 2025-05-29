package com.decoder.model;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String email;

    public JwtResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }

    // Getters
    public String getToken() { return token; }
    public String getType() { return type; }
    public String getEmail() { return email; }

    // Optional: Setters (only needed if using in request bodies or for mutation)
    public void setToken(String token) { this.token = token; }
    public void setType(String type) { this.type = type; }
    public void setEmail(String email) { this.email = email; }
}
