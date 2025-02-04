package com.aitravel.application.service;

import com.aitravel.application.config.CustomUserDetails;
import com.aitravel.application.dto.user.UserAuthResponse;
import com.aitravel.application.dto.user.LoginRequest;
import com.aitravel.application.dto.user.UserRequestDTO;
import com.aitravel.application.exceptions.AuthenticationFailedException;
import com.aitravel.application.exceptions.UserAlreadyExistsException;
import com.aitravel.application.exceptions.UserNotFoundException;
import com.aitravel.application.model.Profile;
import com.aitravel.application.model.User;
import com.aitravel.application.repository.ProfileRepository;
import com.aitravel.application.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @PersistenceContext
    private EntityManager entityManager;  // Inject EntityManager

    @Transactional
    public UserAuthResponse signup(UserRequestDTO request) {
        // Check if user already exists
        if (userRepository.existsByName(request.getName())) {
            throw new UserAlreadyExistsException("User with name '" + request.getName() + "' already exists.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email '" + request.getEmail() + "' already exists.");
        }

        // Create and save user
        User user = User.builder()
                .userId(UUID.randomUUID().toString().replace("-", ""))
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .avatarImgUrl(request.getAvatarImgUrl())
                .build();

        log.info("Saving user: {}", user.getEmail());
        User savedUser = userRepository.save(user);
        log.info("User saved with ID: {}", savedUser.getUserId());

        // Re-attach the saved user to the persistence context
        User attachedUser = entityManager.merge(savedUser);

        // Create profile
        Profile profile = Profile.builder()
                .userId(attachedUser.getUserId())  // Use the attached user's ID
                .user(attachedUser)  // Set the attached user reference
                .location(null)
                .bio(null)
                .website(null)
                .joinDate(LocalDateTime.now())
                .instagramUrl(null)
                .facebookUrl(null)
                .twitterUrl(null)
                .linkedinUrl(null)
                .youtubeUrl(null)
                .phone(null)
                .languages(new String[0])
                .build();

        log.info("Saving profile for user ID: {}", attachedUser.getUserId());
        entityManager.persist(profile);
        log.info("Profile saved successfully for user ID: {}", attachedUser.getUserId());

        return UserAuthResponse.builder()
                .message("User registered successfully")
                .userId(attachedUser.getUserId())
                .avatarImgUrl(attachedUser.getAvatarImgUrl())
                .name(attachedUser.getName())
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
                        .avatarImgUrl(user.getAvatarImgUrl())
                        .name(user.getName())
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


