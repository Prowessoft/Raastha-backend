package com.aitravel.application.repositoryjpa;

import com.aitravel.application.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DayRepository extends JpaRepository<Day, UUID> {
}
