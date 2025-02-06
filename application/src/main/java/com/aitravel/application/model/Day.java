package com.aitravel.application.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "days", uniqueConstraints = @UniqueConstraint(columnNames = {"itinerary_id", "day_number"}))
public class Day {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    private LocalDate date;
    private String notes;

    @Column(name = "planned_budget", precision = 10, scale = 2)
    private BigDecimal plannedBudget;

    @Column(name = "actual_budget", precision = 10, scale = 2)
    private BigDecimal actualBudget;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // One-to-many relationship with Activity (for sections mapping)
    // Collection of activities (includes hotels, attractions, restaurants)
    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();

    // Travel routes for the day (each route connects two activities)
    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TravelRoute> travelRoutes = new ArrayList<>();
}
