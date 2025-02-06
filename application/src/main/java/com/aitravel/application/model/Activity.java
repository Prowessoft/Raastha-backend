package com.aitravel.application.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;



import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    private UUID id;

// Link back to the Day
    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private Day day;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    // Activity-specific fields
    private LocalTime startTime;
    private LocalTime endTime;

    // If duration is provided as string in the DTO you may compute it or store it as INTERVAL
    @Column(name = "duration")
    private String duration;

    private Double price;

    // You may store notes or booking info here if needed
    private String notes;

    private Integer sequenceNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "confirmationNumber", column = @Column(name = "confirmation_number")),
            @AttributeOverride(name = "reservationNumber", column = @Column(name = "reservation_number")),
            @AttributeOverride(name = "notes", column = @Column(name = "booking_info_notes"))
    })
    private BookingInfo bookingInfo;

}
