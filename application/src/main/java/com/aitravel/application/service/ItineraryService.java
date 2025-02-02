package com.aitravel.application.service;

import com.aitravel.application.dto.requestdtos.DayRequest;
import com.aitravel.application.dto.requestdtos.ItineraryRequest;
import com.aitravel.application.dto.requestdtos.LocationRequest;
import com.aitravel.application.dto.responsedtos.ItineraryBasicResponse;
import com.aitravel.application.dto.responsedtos.ItineraryResponse;
import com.aitravel.application.model.Itinerary;
import com.aitravel.application.model.ItineraryDay;
import com.aitravel.application.model.ItineraryLocation;
import com.aitravel.application.model.User;
import com.aitravel.application.objectmapper.ItineraryMapper;
import com.aitravel.application.repository.ItineraryRepository;
import com.aitravel.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItineraryService {
    private final UserRepository userRepository;
    private final ItineraryRepository itineraryRepository;
    private final ItineraryMapper itineraryMapper;

    /**
     * Saves an itinerary along with its associated days, locations, and routes.
     * Ensures that all relationships are properly persisted.
     *
     * @param request The itinerary request containing all necessary information
     */
    public void saveItinerary(ItineraryRequest request) {
        log.info("Saving simplified itinerary for user ID: {}", request.getUserId());

        // Fetch the user by ID
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        // Initialize the itinerary
        Itinerary itinerary = new Itinerary();
        itinerary.setUser(user);
        itinerary.setTitle(request.getTitle());
        itinerary.setDescription(request.getDescription());

        // Create days and locations
        for (DayRequest dayRequest : request.getDays()) {
            ItineraryDay day = new ItineraryDay();
            day.setItinerary(itinerary);
            day.setDayNumber(dayRequest.getDayNumber());

            // Add locations to the day
            for (LocationRequest locationRequest : dayRequest.getLocations()) {
                ItineraryLocation location = new ItineraryLocation();
                location.setDay(day);
                location.setName(locationRequest.getName());
                location.setLatitude(locationRequest.getLatitude());
                location.setLongitude(locationRequest.getLongitude());

                day.getLocations().add(location);
            }

            itinerary.getDays().add(day);
        }

        // Save the itinerary with cascading days and locations
        itineraryRepository.save(itinerary);

        log.info("Itinerary with ID: {} saved successfully", itinerary.getItineraryId());
    }

    /**
     * Fetches basic itineraries for a user by userId.
     * Returns only the itinerary table information without related entities.
     */
    public List<ItineraryBasicResponse> getBasicItinerariesByUserId(Long userId) {
        log.info("Fetching itinerary table information for user ID: {}", userId);
        return itineraryRepository.findByUserUserId(userId).stream()
                .map(itinerary -> new ItineraryBasicResponse(
                        itinerary.getItineraryId(),
                        itinerary.getUser().getUserId(),
                        itinerary.getTitle(),
                        itinerary.getDescription()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Fetches complete itinerary details by itinerary ID, including all days, locations, and routes.
     */
    public ItineraryResponse getCompleteItineraryById(UUID itineraryId) {
        log.info("Fetching complete details for itinerary ID: {}", itineraryId);

        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found with id: " + itineraryId));

        return itineraryMapper.toResponse(itinerary);
    }
}
