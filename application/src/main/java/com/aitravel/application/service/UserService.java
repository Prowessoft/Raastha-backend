package com.aitravel.application.service;

import com.aitravel.application.config.CustomUserDetails;
import com.aitravel.application.dto.user.UserAuthResponse;
import com.aitravel.application.dto.user.LoginRequest;
import com.aitravel.application.dto.user.UserRequestDTO;
import com.aitravel.application.exceptions.AuthenticationFailedException;
import com.aitravel.application.exceptions.UserAlreadyExistsException;
import com.aitravel.application.exceptions.UserNotFoundException;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserAuthResponse signup(UserRequestDTO request) {
        if (userRepository.existsByName(request.getName())) {
            throw new UserAlreadyExistsException("User with name '" + request.getName() + "' already exists.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email '" + request.getEmail() + "' already exists.");
        }

        User user = User.builder()
                .userId(UUID.randomUUID().toString().replace("-",""))
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .avatarImgUrl(request.getAvatarImgUrl())
                .build();

        User savedUser = userRepository.save(user);
        log.info("New user registered: {}", savedUser.getEmail());

        return UserAuthResponse.builder()
                .message("User registered successfully")
                .userId(savedUser.getUserId())
                .build();
    }

    public UserAuthResponse login(LoginRequest request) {
        try {
            // Authenticate using email
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                User user = userDetails.getUser();

                log.info("User logged in: {}", user.getEmail());
                return UserAuthResponse.builder()
                        .userId(user.getUserId())
                        .message("Login successful")
                        .build();
            } else {
                throw new AuthenticationFailedException("Invalid email or password.");
            }
        } catch (AuthenticationException e) {
            log.error("Authentication failure for user {}: {}", request.getEmail(), e.getMessage());
            throw new AuthenticationFailedException("Invalid email or password.");
        }
    }

    public User getUserDetails(String userId) {
        log.info("Fetching user details for user ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID '" + userId + "' not found."));
    }
}


