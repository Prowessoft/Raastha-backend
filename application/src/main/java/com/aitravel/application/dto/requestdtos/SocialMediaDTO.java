package com.aitravel.application.dto.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialMediaDTO {
    private String instagram;
    private String facebook;
    private String twitter;
    private String linkedin;
    private String youtube;
}
