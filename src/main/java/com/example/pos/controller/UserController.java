package com.example.pos.controller;

import com.example.pos.dto.user.UserRequest;
import com.example.pos.model.Role;
import com.example.pos.model.User;

import com.example.pos.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, HttpServletRequest request) {
        Long tokenUserId = (Long) request.getAttribute("userId");

        if (!id.equals(tokenUserId) && request.getAttribute("userRole") != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody UserRequest request) {
        User saved = userService.createUser(request);
        return ResponseEntity.ok(saved);
    }
}

