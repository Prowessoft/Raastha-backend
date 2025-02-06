package com.aitravel.application.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    private UUID id;
    @ManyToOne
    @JoinColumn(name="place_id", nullable=false)
    private Place place;

    @Column(nullable=false)
    private String url;
    private String caption;

    @Column(name="created_at")
    private LocalDateTime createdAt;

}
