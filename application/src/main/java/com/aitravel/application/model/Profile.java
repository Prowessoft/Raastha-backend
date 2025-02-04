package com.aitravel.application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Profile {
    @Id
    @Column(name = "user_id", length = 32)
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;  // Add JsonIgnoreProperties here

    private String location;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String website;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "twitter_url")
    private String twitterUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "youtube_url")
    private String youtubeUrl;

    private String phone;

    @Column(columnDefinition = "text[]")
    private String[] languages;
}
