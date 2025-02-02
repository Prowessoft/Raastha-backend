package com.aitravel.application.service;

import com.aitravel.application.dto.user.AuthResponse;
import com.aitravel.application.dto.user.LoginRequest;
import com.aitravel.application.dto.user.UserRequestDTO;
import com.aitravel.application.model.User;
import com.aitravel.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signup(UserRequestDTO request) {
        if (userRepository.existsByName(request.getName())) {
            throw new RuntimeException("Name already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .avatarImgUrl(request.getAvatarImgUrl())
                .build();

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully")
                .user(savedUser)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getName(),
                            request.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                User user = userRepository.findByName(request.getName())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                return AuthResponse.builder()
                        .message("Login successful")
                        .user(user)
                        .build();
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // 4. Get user details by userId
    public User getUserDetails(Long userId) {
        log.info("Fetching user details for ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}


