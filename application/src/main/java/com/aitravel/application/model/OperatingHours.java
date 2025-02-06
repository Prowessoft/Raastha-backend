package com.aitravel.application.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "operating_hours", uniqueConstraints = @UniqueConstraint(columnNames = {"place_id", "day_of_week"}))
public class OperatingHours {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    private UUID id;

// Many operating hours records belong to one Place
    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 0=Sunday, 1=Monday, etc.

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

}