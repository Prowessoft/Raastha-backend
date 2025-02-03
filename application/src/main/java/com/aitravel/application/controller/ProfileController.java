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
        return ResponseEntity.ok(profileService.saveProfile(userId, profileRequest));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }
}

