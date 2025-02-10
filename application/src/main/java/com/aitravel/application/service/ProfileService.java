package com.aitravel.application.service;

import com.aitravel.application.dto.requestdtos.ProfileRequestDTO;
import com.aitravel.application.dto.responsedtos.ProfileResponseDTO;
import com.aitravel.application.exceptions.ProfileNotFoundException;
import com.aitravel.application.exceptions.UserNotFoundException;
import com.aitravel.application.model.Profile;
import com.aitravel.application.model.User;
import com.aitravel.application.repositoryjpa.ProfileRepository;
import com.aitravel.application.repositoryjpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProfileResponseDTO saveProfile(String userId, ProfileRequestDTO requestDTO) {
        log.info("Received request to save profile for userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserNotFoundException("User not found with ID: " + userId);
                });
        log.debug("User found: {}", user);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.debug("No existing profile found for userId: {}. Creating new profile.", userId);
                    return Profile.builder().user(user).build();
                });

        // Convert List<String> to String[]
        log.debug("Setting languages for profile from request: {}", requestDTO.getLanguages());
        profile.setLanguages(requestDTO.getLanguages().toArray(new String[0]));

        // Set other fields
        if (requestDTO.getName() != null) {
            log.debug("Updating user's name to: {}", requestDTO.getName());
            user.setName(requestDTO.getName());
        }
        profile.setLocation(requestDTO.getLocation());
        profile.setBio(requestDTO.getBio());
        profile.setWebsite(requestDTO.getWebsite());
        profile.setJoinDate(user.getCreatedAt());
        profile.setPhone(requestDTO.getPhone());
        log.debug("Profile basic fields set. Location: {}, Bio: {}, Website: {}, Phone: {}",
                requestDTO.getLocation(), requestDTO.getBio(), requestDTO.getWebsite(), requestDTO.getPhone());

        // Set social media URLs from Map
        Map<String, String> socialMedia = requestDTO.getSocialMedia();
        if (socialMedia != null) {
            log.debug("Setting social media URLs for profile: {}", socialMedia);
            profile.setInstagramUrl(socialMedia.get("instagram"));
            profile.setFacebookUrl(socialMedia.get("facebook"));
            profile.setTwitterUrl(socialMedia.get("twitter"));
            profile.setLinkedinUrl(socialMedia.get("linkedin"));
            profile.setYoutubeUrl(socialMedia.get("youtube"));
        } else {
            log.debug("No social media information provided in request.");
        }

        Profile savedProfile = profileRepository.save(profile);
        log.info("Profile saved successfully for userId: {}", userId);
        return createProfileResponse(user, savedProfile);
    }

    public ProfileResponseDTO getProfile(String userId) {
        log.info("Received request to get profile for userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserNotFoundException("User not found with ID: " + userId);
                });
        log.debug("User found: {}", user);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Profile not found for user ID: {}", userId);
                    return new ProfileNotFoundException("Profile not found for user ID: " + userId);
                });
        log.info("Profile retrieved successfully for userId: {}", userId);
        return createProfileResponse(user, profile);
    }

    private ProfileResponseDTO createProfileResponse(User user, Profile profile) {
        log.debug("Creating ProfileResponseDTO for userId: {} using profile id: {}", user.getUserId(), profile.getUserId());
        // Convert social media URLs to Map
        Map<String, String> socialMedia = new HashMap<>();
        socialMedia.put("instagram", profile.getInstagramUrl());
        socialMedia.put("facebook", profile.getFacebookUrl());
        socialMedia.put("twitter", profile.getTwitterUrl());
        socialMedia.put("linkedin", profile.getLinkedinUrl());
        socialMedia.put("youtube", profile.getYoutubeUrl());

        ProfileResponseDTO responseDTO = ProfileResponseDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .avatarImgUrl(user.getAvatarImgUrl())
                .location(profile.getLocation())
                .bio(profile.getBio())
                .website(profile.getWebsite())
                .joinDate(profile.getJoinDate())
                .socialMedia(socialMedia)
                .phone(profile.getPhone())
                .languages(profile.getLanguages() != null ?
                        Arrays.asList(profile.getLanguages()) :
                        new ArrayList<>())
                .updatedAt(user.getUpdatedAt())
                .build();
        log.debug("ProfileResponseDTO created: {}", responseDTO);
        return responseDTO;
    }
}
