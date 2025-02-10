package com.aitravel.application.dto.responsedtos;

import com.aitravel.application.dto.ItineraryDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class ItineraryResponse {
    private UUID id;
    private String userId;
    private String message;
    private LocalDateTime createdAt;

    // Factory method to create response from DTO and message
    public static ItineraryResponse fromDtoWithMessage(ItineraryDTO dto, String message) {
        ItineraryResponse response = new ItineraryResponse();
        response.setId(dto.getId());
        response.setUserId(dto.getUserId());
        response.setMessage(message);
        response.setCreatedAt(dto.getCreatedAt());
        return response;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}