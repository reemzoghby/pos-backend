package com.example.pos.service;

import com.example.pos.common.NotFoundException;
import com.example.pos.dto.user.UserRequest;
import com.example.pos.model.Role;
import com.example.pos.model.User;
import com.example.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public User createUser(UserRequest request) {
        User user = new User();
        user.setFirstName(request.firstName);
        user.setLastName(request.lastName);
        user.setEmail(request.email);
        user.setPassword(encoder.encode(request.password));
        user.setRole(Role.valueOf(request.role.toUpperCase()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean validatePassword(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }
}