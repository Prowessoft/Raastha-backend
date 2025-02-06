package com.aitravel.application.objectmapper;
import com.aitravel.application.dto.responsedtos.DayResponse;
import com.aitravel.application.dto.responsedtos.ItineraryResponse;
import com.aitravel.application.dto.responsedtos.LocationResponse;
import com.aitravel.application.model.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ItineraryMapperOld {

    public ItineraryResponse toResponse(ItineraryOld itinerary) {
        return new ItineraryResponse(
                itinerary.getUser().getUserId(),
                itinerary.getTitle(),
                itinerary.getDescription(),
                itinerary.getDays().stream().map(day -> new DayResponse(
                        day.getDayNumber(),
                        day.getLocations().stream().map(location -> new LocationResponse(
                                location.getName(),
                                location.getLatitude(),
                                location.getLongitude()
                        )).collect(Collectors.toList())
                )).collect(Collectors.toList())
        );
    }
}
