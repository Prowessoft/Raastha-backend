package com.aitravel.application.controller;

import com.aitravel.application.dto.ItineraryBasicDTO;
import com.aitravel.application.dto.ItineraryDTO;
import com.aitravel.application.exceptions.ResourceNotFoundException;
import com.aitravel.application.service.ItineraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/itineraries")
@Slf4j
public class ItineraryController {

    private final ItineraryService itineraryService;

    @Autowired
    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createItinerary(@RequestBody Map<String, ItineraryDTO> payload) {
        log.info("Received request to create itinerary with payload: {}", payload);
        ItineraryDTO dto = payload.get("itinerary"); // Unwrap the payload
        if (dto == null) {
            log.warn("Itinerary payload is null");
            return ResponseEntity.badRequest().build();
        }
        ItineraryDTO created = itineraryService.createItinerary(dto);
        log.info("Itinerary created successfully with id: {}", created.getId());
        return new ResponseEntity<>("Itinerary Created Successfully with ItineraryId: " + created.getId() , HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItineraryDTO>> getItinerariesByUser(@PathVariable String userId) {
        log.info("Fetching itineraries for user with id: {}", userId);
        List<ItineraryDTO> itineraries = itineraryService.getItinerariesByUserId(userId);
        log.info("Found {} itineraries for user {}", itineraries.size(), userId);
        return ResponseEntity.ok(itineraries);
    }

    @GetMapping("/{itineraryId}")
    public ResponseEntity<ItineraryDTO> getItineraryById(@PathVariable UUID itineraryId) {
        log.info("Fetching itinerary by id: {}", itineraryId);
        ItineraryDTO dto = itineraryService.getItineraryById(itineraryId);
        log.info("Itinerary found with id: {}", dto.getId());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/basic/user/{userId}")
    public ResponseEntity<List<ItineraryBasicDTO>> getItineraryBasicByUser(@PathVariable String userId) {
        log.info("Fetching basic itineraries for user with id: {}", userId);
        List<ItineraryBasicDTO> basicItineraries = itineraryService.getItineraryBasicByUserId(userId);
        log.info("Found {} basic itineraries for user {}", basicItineraries.size(), userId);
        return ResponseEntity.ok(basicItineraries);
    }

//    @PostMapping("/upsert")
//    public ResponseEntity<ItineraryDTO> upsertItinerary(@RequestBody Map<String, ItineraryDTO> payload) {
//        ItineraryDTO dto = payload.get("itinerary"); // Unwrap the payload
//        if (dto == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        // Check if it's a create or update based on id presence
//        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
//            ItineraryDTO created = itineraryService.createItinerary(dto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(created);
//        } else {
//            ItineraryDTO updated = itineraryService.updateItinerary(UUID.fromString(dto.getId()), dto);
//            return ResponseEntity.ok(updated);
//        }
//    }

    @DeleteMapping("/{itineraryId}")
    public ResponseEntity<Void> deleteItinerary(@PathVariable UUID itineraryId) {
        log.info("Deleting itinerary with id: {}", itineraryId);
        itineraryService.deleteItinerary(itineraryId);
        log.info("Itinerary with id {} deleted successfully", itineraryId);
        return ResponseEntity.noContent().build();
    }

}
