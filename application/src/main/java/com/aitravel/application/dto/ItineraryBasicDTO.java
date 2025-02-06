package com.aitravel.application.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItineraryBasicDTO {
    private String id;
    private String userId;
    private String title;
    private String status;
    private String visibility;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String destinationName;
    private String destinationCoordinates;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer version;
    private Boolean isTemplate;
    private String language;
}
