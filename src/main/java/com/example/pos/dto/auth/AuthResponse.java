package com.example.pos.dto.auth;

public class AuthResponse {
    public String accessToken;
    public String refreshToken;
    public String role;

    public AuthResponse(String accessToken, String refreshToken, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }
}
