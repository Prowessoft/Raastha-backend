package com.aitravel.application.dto.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ItineraryBasicResponse {
    private UUID itineraryId;
    private String userId;
    private String title;
    private String description;
}

