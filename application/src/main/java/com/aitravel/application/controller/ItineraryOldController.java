//package com.aitravel.application.controller;
//
//import com.aitravel.application.dto.responsedtos.ItineraryBasicResponse;
//import com.aitravel.application.dto.requestdtos.ItineraryRequest;
//import com.aitravel.application.dto.responsedtos.ItineraryResponse;
//import com.aitravel.application.service.ItineraryService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/itinerary")
//@RequiredArgsConstructor
//@Slf4j
//@CrossOrigin(origins = "*")
//public class ItineraryController {
//    private final ItineraryService itineraryService;
//
//    @PostMapping("/save")
//    public ResponseEntity<String> saveItinerary(@RequestBody ItineraryRequest request) {
//        itineraryService.saveItinerary(request);
//        return ResponseEntity.ok("Itinerary saved successfully!");
//    }
//
//    /**
//     * Get itinerary table information by userId.
//     *
//     * @param userId The ID of the user
//     * @return List of itineraries (only fields from the itinerary table)
//     */
//    @GetMapping("/user/{userId}/basic")
//    public ResponseEntity<List<ItineraryBasicResponse>> getBasicItineraries(@PathVariable String userId) {
//        try {
//            List<ItineraryBasicResponse> itineraries = itineraryService.getBasicItinerariesByUserId(userId);
//            return ResponseEntity.ok(itineraries);
//        } catch (Exception e) {
//            log.error("Error fetching itinerary table information: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/{itineraryId}/details")
//    public ResponseEntity<ItineraryResponse> getItineraryDetails(@PathVariable UUID itineraryId) {
//        ItineraryResponse itinerary = itineraryService.getCompleteItineraryById(itineraryId);
//        return ResponseEntity.ok(itinerary);
//    }
//}
//
