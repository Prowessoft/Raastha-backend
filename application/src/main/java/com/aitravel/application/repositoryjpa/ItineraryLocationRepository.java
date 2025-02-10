package com.aitravel.application.repositoryjpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aitravel.application.model.ItineraryLocation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItineraryLocationRepository extends JpaRepository<ItineraryLocation, Long> {
    Optional<ItineraryLocation> findByLocationId(Long locationId);
}
