package com.aitravel.application.dto.responsedtos;

import com.aitravel.application.dto.requestdtos.SocialMediaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDTO {
    private String userId;
    private String name;
    private String email;
    private String avatarImgUrl;
    private String location;
    private String bio;
    private String website;
    private LocalDateTime joinDate;
    @Builder.Default
    private Map<String, String> socialMedia = new HashMap<>();
    private String phone;
    @Builder.Default
    private List<String> languages = new ArrayList<>();
    private LocalDateTime updatedAt;
}
