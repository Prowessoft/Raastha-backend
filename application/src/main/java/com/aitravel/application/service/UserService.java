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
import com.aitravel.application.repositoryjpa.ProfileRepository;
import com.aitravel.application.repositoryjpa.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        log.info("Received signup request for user with email: {}", request.getEmail());
        try {
            // Check if user already exists based on name
            if (userRepository.existsByName(request.getName())) {
                log.error("Signup failed: User with name '{}' already exists.", request.getName());
                throw new UserAlreadyExistsException("User with name '" + request.getName() + "' already exists.");
            }

            // Check if user already exists based on email
            if (userRepository.existsByEmail(request.getEmail())) {
                log.error("Signup failed: User with email '{}' already exists.", request.getEmail());
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

            log.info("Saving new user with email: {}", user.getEmail());
            User savedUser = userRepository.save(user);
            log.info("User saved successfully with ID: {}", savedUser.getUserId());

            // Re-attach the saved user to the persistence context for further operations
            User attachedUser = entityManager.merge(savedUser);
            log.debug("User re-attached to persistence context with ID: {}", attachedUser.getUserId());

            // Create profile for the new user
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
        } catch (UserAlreadyExistsException e) {
            throw e; // Re-throw the exception so the calling method can handle it
        } catch (DataAccessException e) {
            log.error("Error during signup for user with email: {}", request.getEmail(), e);
            throw new RuntimeException("Failed to signup due to database error.", e);
        } catch (Exception e) {
            log.error("Unexpected error during signup for user with email: {}", request.getEmail(), e);
            throw new RuntimeException("Failed to signup due to unexpected error.", e);
        }
    }

    public UserAuthResponse login(LoginRequest request) {
        log.info("Received login request for email: {}", request.getEmail());
        try {
            // Authenticate using email and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                User user = userDetails.getUser();
                log.info("User logged in successfully: {}", user.getEmail());
                return UserAuthResponse.builder()
                        .userId(user.getUserId())
                        .message("Login successful")
                        .avatarImgUrl(user.getAvatarImgUrl())
                        .name(user.getName())
                        .build();
            } else {
                log.error("Authentication failed for email: {}. Invalid email or password.", request.getEmail());
                throw new AuthenticationFailedException("Invalid email or password.");
            }
        } catch (AuthenticationException e) {
            log.error("Authentication error for email: {}: {}", request.getEmail(), e.getMessage());
            throw new AuthenticationFailedException("Invalid email or password.");
        }
    }

    public User getUserDetails(String userId) {
        log.info("Fetching user details for user ID: {}", userId);
        try {
            return userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.error("User not found with ID: {}", userId);
                        return new UserNotFoundException("User with ID '" + userId + "' not found.");
                    });
        } catch (UserNotFoundException e) {
            throw e; // Re-throw the exception so the calling method can handle it
        } catch (DataAccessException e) {
            log.error("Error fetching user details for user ID: {}", userId, e);
            throw new RuntimeException("Failed to retrieve user details due to database error.", e);
        } catch (Exception e) {
            log.error("Unexpected error fetching user details for user ID: {}", userId, e);
            throw new RuntimeException("Failed to retrieve user details due to unexpected error.", e);
        }
    }
}
