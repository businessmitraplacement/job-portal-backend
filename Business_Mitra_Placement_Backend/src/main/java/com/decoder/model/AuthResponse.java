package com.decoder.model;

public class AuthResponse {
    private String message;
    private String accessToken;
    private String tokenType = "Bearer";

    // Constructor with accessToken only
    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // ✅ Constructor with message and accessToken
    public AuthResponse(String message, String accessToken) {
        this.message = message;
        this.accessToken = accessToken;
    }

    // Getters and setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
