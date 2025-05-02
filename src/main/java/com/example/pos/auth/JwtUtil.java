package com.example.pos.auth;

import com.example.pos.model.Role;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "p53X1!vKz2N^sLjF4aG7rT8uBq9XeWdC3mZyKpHr8NvDfVgBhRtQsLuYxEzMaWcB";

    public String generateToken(Long userId, String email, Role role) {
        long expirationMs = 84000000;

        return Jwts.builder()
                .setSubject(email)
                .claim("email", email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Role extractRole(String token) {
        String roleStr = (String) extractAllClaims(token).get("role");
        return Role.valueOf(roleStr);
    }

    public Long extractUserId(String token) {
        return ((Number) extractAllClaims(token).get("userId")).longValue();
    }
}
