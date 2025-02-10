package com.aitravel.application.repositoryjpa;

import com.aitravel.application.model.SharedAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;


public interface SharedAccessRepository extends JpaRepository<SharedAccess, UUID> {
    List<SharedAccess> findByItineraryId(UUID itineraryId);
}
