package com.aitravel.application.controller;

import com.aitravel.application.dto.user.UserAuthResponse;
import com.aitravel.application.dto.user.LoginRequest;
import com.aitravel.application.dto.user.UserRequestDTO;
import com.aitravel.application.exceptions.AuthenticationFailedException;
import com.aitravel.application.exceptions.UserAlreadyExistsException;
import com.aitravel.application.exceptions.UserNotFoundException;
import com.aitravel.application.model.User;
import com.aitravel.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class UserAuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserAuthResponse> signup(@Valid @RequestBody UserRequestDTO request) {
        log.info("Received signup request for email: {}", request.getEmail());
        try {
            UserAuthResponse authResponse = userService.signup(request);
            log.info("Signup successful for email: {}", request.getEmail());
            return ResponseEntity.ok(authResponse);
        } catch (UserAlreadyExistsException e) {
            log.warn("Signup failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Or a more detailed error response
        } catch (Exception e) {
            log.error("Signup failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Or a more detailed error response
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Received login request for email: {}", request.getEmail());
        try {
            UserAuthResponse authResponse = userService.login(request);
            log.info("Login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(authResponse);
        } catch (AuthenticationFailedException e) {
            log.warn("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Or a more detailed error response
        } catch (Exception e) {
            log.error("Login failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Or a more detailed error response
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable String userId) {
        log.info("Received request to fetch details for userId: {}", userId);
        try {
            User user = userService.getUserDetails(userId);
            log.info("Successfully retrieved details for userId: {}", userId);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            log.warn("User not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching user details for userId {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
