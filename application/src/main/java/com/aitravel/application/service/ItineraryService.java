package com.aitravel.application.service;

import com.aitravel.application.dto.ItineraryBasicDTO;
import com.aitravel.application.dto.ItineraryDTO;
import com.aitravel.application.exceptions.ResourceNotFoundException;
import com.aitravel.application.model.Itinerary;
import com.aitravel.application.objectmapper.ItineraryBasicMapper;
import com.aitravel.application.objectmapper.ItineraryMapper;
import com.aitravel.application.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final ItineraryMapper itineraryMapper;

    private final ItineraryBasicMapper itineraryBasicMapper;

    @Autowired
    public ItineraryService(ItineraryRepository itineraryRepository, ItineraryMapper itineraryMapper, ItineraryBasicMapper itineraryBasicMapper) {
        this.itineraryRepository = itineraryRepository;
        this.itineraryMapper = itineraryMapper;
        this.itineraryBasicMapper = itineraryBasicMapper;

    }

    public ItineraryDTO createItinerary(ItineraryDTO itineraryDTO) {
        Itinerary itinerary = itineraryMapper.toEntity(itineraryDTO);
        itinerary.setCreatedAt(LocalDateTime.now());
        itinerary.setUpdatedAt(LocalDateTime.now());
        Itinerary savedItin = itineraryRepository.save(itinerary);
        return itineraryMapper.toDTO(savedItin);
    }

    public List<ItineraryDTO> getItinerariesByUserId(String userId) {
        return itineraryRepository.findByUserId(userId)
                .stream()
                .map(itineraryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ItineraryDTO getItineraryById(UUID itineraryId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary not found with id: " + itineraryId));
        return itineraryMapper.toDTO(itinerary);
    }

    public List<ItineraryBasicDTO> getItineraryBasicByUserId(String userId) {
        return itineraryRepository.findByUserId(userId)
                .stream()
                .map(itineraryBasicMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ItineraryDTO updateItinerary(UUID itineraryId, ItineraryDTO itineraryDTO) {
        // Retrieve the existing itinerary from the database
        Itinerary existing = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary not found with id: " + itineraryId));
        // Update fields on the existing entity using the mapper merge/update method.
        itineraryMapper.updateEntity(existing, itineraryDTO);
        // Update the timestamp.
        existing.setUpdatedAt(LocalDateTime.now());
        Itinerary updated = itineraryRepository.save(existing);
        return itineraryMapper.toDTO(updated);
    }

}
