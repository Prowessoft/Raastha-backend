package com.aitravel.application.objectmapper;

import com.aitravel.application.dto.ItineraryBasicDTO;
import com.aitravel.application.model.Itinerary;
import org.springframework.stereotype.Component;
@Component
public class ItineraryBasicMapper {
    public ItineraryBasicDTO toDTO(Itinerary itinerary) {
        if (itinerary == null) return null;
        ItineraryBasicDTO dto = new ItineraryBasicDTO();
        dto.setId(itinerary.getId().toString());
        dto.setUserId(itinerary.getUserId());
        dto.setTitle(itinerary.getTitle());
        dto.setStatus(itinerary.getStatus());
        dto.setVisibility(itinerary.getVisibility());
        dto.setCreatedAt(itinerary.getCreatedAt());
        dto.setUpdatedAt(itinerary.getUpdatedAt());
        dto.setDestinationName(itinerary.getDestinationName());
        dto.setDestinationCoordinates(itinerary.getDestinationCoordinates());
        dto.setStartDate(itinerary.getStartDate());
        dto.setEndDate(itinerary.getEndDate());
        dto.setVersion(itinerary.getVersion());
        dto.setIsTemplate(itinerary.getIsTemplate());
        dto.setLanguage(itinerary.getLanguage());
        return dto;
    }

}