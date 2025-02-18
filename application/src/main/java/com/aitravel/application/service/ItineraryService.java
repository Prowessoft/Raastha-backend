package com.aitravel.application.service;

import com.aitravel.application.dto.ItineraryBasicDTO;
import com.aitravel.application.dto.ItineraryDTO;
import com.aitravel.application.exceptions.ResourceNotFoundException;
import com.aitravel.application.model.Itinerary;
import com.aitravel.application.objectmapper.ItineraryBasicMapper;
import com.aitravel.application.objectmapper.ItineraryMapper;
import com.aitravel.application.repositoryjpa.ItineraryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItineraryService {

    private static final Logger log = LoggerFactory.getLogger(ItineraryService.class);

    private final ItineraryRepository itineraryRepository;
    private final ItineraryMapper itineraryMapper;
    private final ItineraryBasicMapper itineraryBasicMapper;

    @Autowired
    public ItineraryService(ItineraryRepository itineraryRepository, ItineraryMapper itineraryMapper, ItineraryBasicMapper itineraryBasicMapper) {
        this.itineraryRepository = itineraryRepository;
        this.itineraryMapper = itineraryMapper;
        this.itineraryBasicMapper = itineraryBasicMapper;
        log.info("Initialized ItineraryService");
    }

    public ItineraryDTO createItinerary(ItineraryDTO itineraryDTO) {
        log.info("Creating itinerary for userId: {}", itineraryDTO.getUserId());
        try {
            if (itineraryDTO.getId() != null) {
                log.debug("ItineraryDTO already has an ID ({}). Deleting existing itinerary.", itineraryDTO.getId());
                itineraryRepository.deleteById(itineraryDTO.getId());
            }
            // Retaining existing ID or null is expected as per business logic.
            itineraryDTO.setId(itineraryDTO.getId());
            Itinerary itinerary = itineraryMapper.toEntity(itineraryDTO);
            itinerary.setCreatedAt(LocalDateTime.now());
            itinerary.setUpdatedAt(LocalDateTime.now());
            log.debug("Saving itinerary entity with ID: {}", itinerary.getId());
            Itinerary savedItin = itineraryRepository.save(itinerary);
            log.info("Itinerary created with ID: {}", savedItin.getId());
            return itineraryMapper.toDTO(savedItin);
        } catch (DataAccessException e) {
            log.error("Error creating itinerary for userId: {}", itineraryDTO.getUserId(), e);
            throw new RuntimeException("Failed to create itinerary due to database error.", e);
        } catch (Exception e) {
            log.error("Unexpected error creating itinerary for userId: {}", itineraryDTO.getUserId(), e);
            throw new RuntimeException("Failed to create itinerary due to unexpected error.", e);
        }
    }

    public List<ItineraryDTO> getItinerariesByUserId(String userId) {
        log.info("Retrieving itineraries for userId: {}", userId);
        try {
            List<ItineraryDTO> itineraries = itineraryRepository.findByUserId(userId)
                    .stream()
                    .map(itineraryMapper::toDTO)
                    .collect(Collectors.toList());
            log.info("Found {} itineraries for userId: {}", itineraries.size(), userId);
            return itineraries;
        } catch (DataAccessException e) {
            log.error("Error retrieving itineraries for userId: {}", userId, e);
            throw new RuntimeException("Failed to retrieve itineraries due to database error.", e);
        } catch (Exception e) {
            log.error("Unexpected error retrieving itineraries for userId: {}", userId, e);
            throw new RuntimeException("Failed to retrieve itineraries due to unexpected error.", e);
        }
    }

    public ItineraryDTO getItineraryById(UUID itineraryId) {
        log.info("Retrieving itinerary with ID: {}", itineraryId);
        try {
            Itinerary itinerary = itineraryRepository.findById(itineraryId)
                    .orElseThrow(() -> {
                        log.error("Itinerary not found with id: {}", itineraryId);
                        return new ResourceNotFoundException("Itinerary not found with id: " + itineraryId);
                    });
            log.info("Itinerary retrieved with ID: {}", itineraryId);
            return itineraryMapper.toDTO(itinerary);
        } catch (DataAccessException e) {
            log.error("Error retrieving itinerary with ID: {}", itineraryId, e);
            throw new RuntimeException("Failed to retrieve itinerary due to database error.", e);
        } catch (ResourceNotFoundException e) {
            throw e; // Re-throw the exception so the calling method can handle it
        } catch (Exception e) {
            log.error("Unexpected error retrieving itinerary with ID: {}", itineraryId, e);
            throw new RuntimeException("Failed to retrieve itinerary due to unexpected error.", e);
        }
    }

    public List<ItineraryBasicDTO> getItineraryBasicByUserId(String userId) {
        log.info("Retrieving basic itineraries for userId: {}", userId);
        try {
            List<ItineraryBasicDTO> basicItineraries = itineraryRepository.findByUserId(userId)
                    .stream()
                    .map(itineraryBasicMapper::toDTO)
                    .collect(Collectors.toList());
            log.info("Found {} basic itineraries for userId: {}", basicItineraries.size(), userId);
            return basicItineraries;
        } catch (DataAccessException e) {
            log.error("Error retrieving basic itineraries for userId: {}", userId, e);
            throw new RuntimeException("Failed to retrieve basic itineraries due to database error.", e);
        } catch (Exception e) {
            log.error("Unexpected error retrieving basic itineraries for userId: {}", userId, e);
            throw new RuntimeException("Failed to retrieve basic itineraries due to unexpected error.", e);
        }
    }

    //    public ItineraryDTO updateItinerary(UUID itineraryId, ItineraryDTO itineraryDTO) {
    //        // Retrieve the existing itinerary from the database
    //        Itinerary existing = itineraryRepository.findById(itineraryId)
    //                .orElseThrow(() -> new ResourceNotFoundException("Itinerary not found with id: " + itineraryId));
    //        // Update fields on the existing entity using the mapper merge/update method.
    //        itineraryMapper.updateEntity(existing, itineraryDTO);
    //        // Update the timestamp.
    //        existing.setUpdatedAt(LocalDateTime.now());
    //        Itinerary updated = itineraryRepository.save(existing);
    //        return itineraryMapper.toDTO(updated);
    //    }

    public void deleteItinerary(UUID itineraryId) {
        log.info("Deleting itinerary with ID: {}", itineraryId);
        try {
            itineraryRepository.deleteById(itineraryId);
            log.info("Itinerary with ID: {} deleted successfully", itineraryId);
        } catch (DataAccessException e) {
            log.error("Error deleting itinerary with ID: {}", itineraryId, e);
            throw new RuntimeException("Failed to delete itinerary due to database error.", e);
        } catch (Exception e) {
            log.error("Unexpected error deleting itinerary with ID: {}", itineraryId, e);
            throw new RuntimeException("Failed to delete itinerary due to unexpected error.", e);
        }
    }
}
