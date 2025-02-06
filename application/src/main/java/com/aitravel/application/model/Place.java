package com.aitravel.application.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name="google_place_id", unique=true)
    private String googlePlaceId;

    @Column(nullable=false)
    private String name;
    private String description;
    private String address;

    @Column(name="coordinates")
    private String coordinates; // e.g., "48.8566,2.3522"

    @Column(name="place_type", nullable=false)
    private String placeType;  // "hotel", "restaurant", or "activity"

    private Double rating;
    private Integer userRatingsTotal;
    private Integer priceLevel;
    private String website;
    private String phone;

    @Column(name="google_maps_url")
    private String googleMapsUrl;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    // Collection of photos for this place
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    // Operating hours are mapped in a separate entity with a ManyToOne relationship.
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperatingHours> operatingHours = new ArrayList<>();

}