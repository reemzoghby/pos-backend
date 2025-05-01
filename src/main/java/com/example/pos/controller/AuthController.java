/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pos.controller;

/**
 *
 * @author CompuTop
 */
import com.example.pos.dto.AuthRequest;
import com.example.pos.dto.AuthResponse;
import com.example.pos.model.User;
import com.example.pos.security.JwtUtil;
import com.example.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Simple in-memory storage for refresh tokens
    private Map<String, String> refreshTokenStore = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.email);
        if (user == null || !userService.validatePassword(request.password, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = UUID.randomUUID().toString(); // ✅ Generate unique refresh token

        // store refresh token
        refreshTokenStore.put(refreshToken, user.getEmail());
        user.setRefreshToken(refreshToken); // optional for debugging

        return ResponseEntity.ok(new AuthResponse(
                accessToken,
                refreshToken,
                user.getRole().name()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String refreshHeader) {
        if (!refreshHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body("Missing Bearer token");
        }

        String refreshToken = refreshHeader.substring(7); // remove "Bearer "

        String email = refreshTokenStore.get(refreshToken);
        if (email == null) {
            return ResponseEntity.status(403).body("Invalid refresh token");
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        String newAccessToken = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(
                newAccessToken,
                refreshToken, // keep the same one
                user.getRole().name()
        ));
    }
}