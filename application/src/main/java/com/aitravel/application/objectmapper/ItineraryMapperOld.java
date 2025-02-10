package com.aitravel.application.objectmapper;
import com.aitravel.application.dto.ItineraryDTO;
import com.aitravel.application.dto.responsedtos.DayResponse;
import com.aitravel.application.dto.responsedtos.ItineraryResponse;
import com.aitravel.application.dto.responsedtos.LocationResponse;
import com.aitravel.application.model.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ItineraryMapperOld {

    public ItineraryResponse itineraryResponse(ItineraryDTO itineraryDto, String message) {
        ItineraryResponse response = new ItineraryResponse();
        response.setId(itineraryDto.getId());
        response.setUserId(itineraryDto.getUserId());
        response.setMessage(message);
        response.setCreatedAt(itineraryDto.getCreatedAt());
        return response;
    }
}
