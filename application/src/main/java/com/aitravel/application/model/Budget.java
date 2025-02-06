package com.aitravel.application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    private String currency;

    // total and breakdown amounts
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "accommodation_amount", precision = 10, scale = 2)
    private BigDecimal accommodationAmount;

    @Column(name = "activities_amount", precision = 10, scale = 2)
    private BigDecimal activitiesAmount;

    @Column(name = "dining_amount", precision = 10, scale = 2)
    private BigDecimal diningAmount;

    @Column(name = "transport_amount", precision = 10, scale = 2)
    private BigDecimal transportAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
