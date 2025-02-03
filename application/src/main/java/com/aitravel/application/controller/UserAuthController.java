package com.aitravel.application.controller;

import com.aitravel.application.dto.user.UserAuthResponse;
import com.aitravel.application.dto.user.LoginRequest;
import com.aitravel.application.dto.user.UserRequestDTO;
import com.aitravel.application.model.User;
import com.aitravel.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<UserAuthResponse> signup(@RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(userService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable String userId) {
        try {
            User user = userService.getUserDetails(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error fetching user details: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}

