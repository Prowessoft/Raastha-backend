package com.aitravel.application.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Table(name = "itineraries")
public class Itinerary {
    @Id
  /*  @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")*/
    private UUID id;

    // userId stored as String (VARCHAR(32))
    @Column(name = "user_id", nullable = false, length = 32)
    private String userId;
    @Column(name = "trip_img", nullable = false, length = 500)
    private String tripImg;

    private String title;
    private String status;
    private String visibility;

    // These fields come from tripDetails.destination and are stored in our table
    @Column(name = "destination_name")
    private String destinationName;

    // Store coordinates as a string "lat,lng"
    @Column(name = "destination_coordinates")
    private String destinationCoordinates;

    // TripDates stored directly in itineraries
    private LocalDate startDate;
    private LocalDate endDate;

    // Metadata fields
    private Integer version;
    @Column(name = "is_template")
    private Boolean isTemplate;
    private String language;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // OneToOne relationship to budgets
    @OneToOne(mappedBy = "itinerary", cascade = CascadeType.ALL)
    private Budget budget;

    // OneToMany to days
    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Day> days = new ArrayList<>();


    // Shared access mappings; see SharedAccess entity below.
    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharedAccess> sharedAccess;
}
