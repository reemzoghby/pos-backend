package com.example.pos.controller;

import com.example.pos.auth.JwtUtil;
import com.example.pos.dto.auth.AuthRequest;
import com.example.pos.dto.auth.AuthResponse;
import com.example.pos.dto.auth.RefreshTokenRequest;
import com.example.pos.model.User;
import com.example.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private final Map<String, String> refreshTokenStore = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.email);
        if (user == null || !userService.validatePassword(request.password, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = UUID.randomUUID().toString();

        refreshTokenStore.put(refreshToken, user.getEmail());
        user.setRefreshToken(refreshToken);

        return ResponseEntity.ok(new AuthResponse(
                accessToken,
                refreshToken,
                user.getRole().name()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken;

        String email = refreshTokenStore.get(refreshToken);
        if (email == null) {
            return ResponseEntity.status(403).body("Invalid refresh token");
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        String newAccessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(
                newAccessToken,
                refreshToken,
                user.getRole().name()
        ));
    }
}