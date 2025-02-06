package com.aitravel.application.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "shared_access", uniqueConstraints = @UniqueConstraint(columnNames = {"itinerary_id", "user_id"}))
public class SharedAccess {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    // userId is a String (VARCHAR(32))
    @Column(name = "user_id", nullable = false, length = 32)
    private String userId;

    private String email;
    private String permission;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}