package com.aitravel.application.controller;

import com.aitravel.application.dto.ItineraryBasicDTO;
import com.aitravel.application.dto.ItineraryDTO;
import com.aitravel.application.exceptions.ResourceNotFoundException;
import com.aitravel.application.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @Autowired
    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ItineraryDTO> createItinerary(@RequestBody Map<String, ItineraryDTO> payload) {
        ItineraryDTO dto = payload.get("itinerary"); // Unwrap the payload
        if (dto == null) {
            return ResponseEntity.badRequest().build();
        }
        ItineraryDTO created = itineraryService.createItinerary(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItineraryDTO>> getItinerariesByUser(@PathVariable String userId) {
        List<ItineraryDTO> itineraries = itineraryService.getItinerariesByUserId(userId);
        return ResponseEntity.ok(itineraries);
    }

    @GetMapping("/{itineraryId}")
    public ResponseEntity<ItineraryDTO> getItineraryById(@PathVariable UUID itineraryId) {
        ItineraryDTO dto = itineraryService.getItineraryById(itineraryId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/basic/user/{userId}")
    public ResponseEntity<List<ItineraryBasicDTO>> getItineraryBasicByUser(@PathVariable String userId) {
        List<ItineraryBasicDTO> basicItineraries = itineraryService.getItineraryBasicByUserId(userId);
        return ResponseEntity.ok(basicItineraries);
    }

    @PostMapping("/upsert")
    public ResponseEntity<ItineraryDTO> upsertItinerary(@RequestBody Map<String, ItineraryDTO> payload) {
        ItineraryDTO dto = payload.get("itinerary"); // Unwrap the payload
        if (dto == null) {
            return ResponseEntity.badRequest().build();
        }
        // Check if it's a create or update based on id presence
        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
            ItineraryDTO created = itineraryService.createItinerary(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } else {
            ItineraryDTO updated = itineraryService.updateItinerary(UUID.fromString(dto.getId()), dto);
            return ResponseEntity.ok(updated);
        }
    }

}
