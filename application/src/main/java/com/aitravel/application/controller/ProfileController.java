package com.aitravel.application.controller;

import com.aitravel.application.dto.requestdtos.ProfileRequestDTO;
import com.aitravel.application.dto.responsedtos.ProfileResponseDTO;
import com.aitravel.application.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        ProfileResponseDTO response = profileService.saveProfile(userId, profileRequest);
        log.info("Profile saved successfully for userId: {}", userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable String userId) {
        log.info("Received request to get profile for userId: {}", userId);
        ProfileResponseDTO response = profileService.getProfile(userId);
        log.info("Retrieved profile for userId: {}", userId);
        return ResponseEntity.ok(response);
    }
}
