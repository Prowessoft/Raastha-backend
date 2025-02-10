package com.aitravel.application.controller;

import com.aitravel.application.dto.requestdtos.ProfileRequestDTO;
import com.aitravel.application.dto.responsedtos.ProfileResponseDTO;
import com.aitravel.application.exceptions.ProfileNotFoundException;
import com.aitravel.application.exceptions.UserNotFoundException;
import com.aitravel.application.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/{userId}")
    public ResponseEntity<ProfileResponseDTO> saveProfile(
            @PathVariable String userId,
            @RequestBody ProfileRequestDTO profileRequest) {
        log.info("Received request to save profile for userId: {}", userId);
        try {
            ProfileResponseDTO response = profileService.saveProfile(userId, profileRequest);
            log.info("Profile saved successfully for userId: {}", userId);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            log.warn("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error saving profile for userId {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable String userId) {
        log.info("Received request to get profile for userId: {}", userId);
        try {
            ProfileResponseDTO response = profileService.getProfile(userId);
            log.info("Retrieved profile for userId: {}", userId);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException | ProfileNotFoundException e) {
            log.warn("Profile not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error getting profile for userId {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
