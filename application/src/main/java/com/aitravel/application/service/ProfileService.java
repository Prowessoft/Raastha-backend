package com.aitravel.application.service;

import com.aitravel.application.dto.requestdtos.ProfileRequestDTO;
import com.aitravel.application.dto.responsedtos.ProfileResponseDTO;
import com.aitravel.application.exceptions.ProfileNotFoundException;
import com.aitravel.application.exceptions.UserNotFoundException;
import com.aitravel.application.model.Profile;
import com.aitravel.application.model.User;
import com.aitravel.application.repository.ProfileRepository;
import com.aitravel.application.repository.UserRepository;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Profile profile = profileRepository.findByUserId(userId)
                .orElse(Profile.builder().user(user).build());

        // Convert List<String> to String[]
        profile.setLanguages(requestDTO.getLanguages().toArray(new String[0]));

        // Set other fields
        if(requestDTO.getName() != null) {
            user.setName(requestDTO.getName());
        }
        profile.setLocation(requestDTO.getLocation());
        profile.setBio(requestDTO.getBio());
        profile.setWebsite(requestDTO.getWebsite());
        profile.setJoinDate(user.getCreatedAt());
        profile.setPhone(requestDTO.getPhone());

        // Set social media URLs from Map
        Map<String, String> socialMedia = requestDTO.getSocialMedia();
        if (socialMedia != null) {
            profile.setInstagramUrl(socialMedia.get("instagram"));
            profile.setFacebookUrl(socialMedia.get("facebook"));
            profile.setTwitterUrl(socialMedia.get("twitter"));
            profile.setLinkedinUrl(socialMedia.get("linkedin"));
            profile.setYoutubeUrl(socialMedia.get("youtube"));
        }

        Profile savedProfile = profileRepository.save(profile);
        return createProfileResponse(user, savedProfile);
    }

    public ProfileResponseDTO getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for user ID: " + userId));

        return createProfileResponse(user, profile);
    }

    private ProfileResponseDTO createProfileResponse(User user, Profile profile) {
        // Convert social media URLs to Map
        Map<String, String> socialMedia = new HashMap<>();
        socialMedia.put("instagram", profile.getInstagramUrl());
        socialMedia.put("facebook", profile.getFacebookUrl());
        socialMedia.put("twitter", profile.getTwitterUrl());
        socialMedia.put("linkedin", profile.getLinkedinUrl());
        socialMedia.put("youtube", profile.getYoutubeUrl());

        return ProfileResponseDTO.builder()
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
    }

}


