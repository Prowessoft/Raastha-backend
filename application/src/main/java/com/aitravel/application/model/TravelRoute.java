package com.aitravel.application.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "travel_routes")
public class TravelRoute {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    private UUID id;

// Each travel route is associated with a day
    @ManyToOne
    @JoinColumn(name="day_id", nullable = false)
    private Day day;

    // References to activities for the start and end of this travel leg
    @ManyToOne
    @JoinColumn(name="from_activity_id", nullable = false)
    private Activity fromActivity;

    @ManyToOne
    @JoinColumn(name="to_activity_id", nullable = false)
    private Activity toActivity;

//    @Column(precision = 10, scale = 2)
    private Double distance; // in kilometers

    // Duration stored in minutes (INTEGER)
    private Integer duration;

    // Travel mode should be one of: 'driving', 'walking', 'transit', 'cycling'
    private String travelMode;

    private String polyline;

    @Column(name="created_at")
    private LocalDateTime createdAt;

}