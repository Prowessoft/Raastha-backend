package com.aitravel.application.dto.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDTO {
    private String name;
    private String location;
    private String bio;
    private String website;
    @Builder.Default
    private Map<String, String> socialMedia = new HashMap<>();
    private String phone;
    @Builder.Default
    private List<String> languages = new ArrayList<>();
}
