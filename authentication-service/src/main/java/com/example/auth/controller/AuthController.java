package com.example.auth.controller;

import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            log.info("Registration failed: username {} already exists", user.getUsername());
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        log.info("User registered: {}", user.getUsername());
        
        return ResponseEntity.ok(Map.of("token", token, "username", user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    String token = jwtService.generateToken(user.getUsername(), user.getRole());
                    log.info("User logged in: {}", username);
                    return ResponseEntity.ok(Map.of("token", token, "username", username));
                })
                .orElseGet(() -> {
                    log.info("Login failed for user: {}", username);
                    return ResponseEntity.badRequest().body("Invalid credentials");
                });
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtService.validateToken(token)) {
                String username = jwtService.getUsernameFromToken(token);
                return ResponseEntity.ok(username);
            }
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }
}
