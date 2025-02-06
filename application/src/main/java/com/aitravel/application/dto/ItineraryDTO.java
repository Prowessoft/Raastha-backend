package com.aitravel.application.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class ItineraryDTO {
    private String id;
    private String userId;
    private String title;
    private String status;
    private String visibility;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TripDetailsDTO tripDetails;
    private List<DayDTO> days;
    private List<SharedWithDTO> sharedWith;
    private MetadataDTO metadata;

    @Data
    public static class TripDetailsDTO {
        private DestinationDTO destination;
        private LocalDate startDate;
        private LocalDate endDate;
        private TripBudgetDTO budget;
    }

    @Data
    public static class DestinationDTO {
        private String name;
        private List<Double> coordinates;
    }

    @Data
    public static class TripBudgetDTO {
        private String currency;
        private Double total;
        private BudgetBreakdownDTO breakdown;
    }

    @Data
    public static class BudgetBreakdownDTO {
        private Double accommodation;
        private Double activities;
        private Double dining;
        private Double transport;
    }

    @Data
    public static class DayDTO {
        private String id;
        private LocalDate date;
        private Integer dayNumber;
        private DayBudgetDTO budget;
        private SectionsDTO sections;
        private TravelInfoDTO travelInfo;
        private String notes;
    }

    @Data
    public static class DayBudgetDTO {
        private Double planned;
        private Double actual;
    }

    @Data
    public static class SectionsDTO {
        private List<PlaceDTO> hotels;
        private List<PlaceDTO> activities;
        private List<PlaceDTO> restaurants;
    }

    @Data
    public static class PlaceDTO {
        private String id;
        private String type;
        private String title;
        private String description;
        private LocationDTO location;
        private String startTime;
        private String endTime;
        private String duration;
        private Double price;
        private String priceLevel;
        private Double rating;
        private Integer userRatingsTotal;
        private List<PhotoDTO> photos;
        private ContactDTO contact;
        private OperatingHoursDTO operatingHours;
        private BookingInfoDTO bookingInfo;
        // Additional field for restaurants:
        private List<String> cuisine;
    }

    @Data
    public static class LocationDTO {
        private String name;
        private List<Double> coordinates;
        private String placeId;
    }

    @Data
    public static class PhotoDTO {
        private String url;
        private String caption;
    }

    @Data
    public static class ContactDTO {
        private String phone;
        private String website;
        private String googleMapsUrl;
    }

    @Data
    public static class OperatingHoursDTO {
        private Boolean isOpen;
        private List<OperatingPeriodDTO> periods;
    }

    @Data
    public static class OperatingPeriodDTO {
        private String day;
        private String hours;
    }

    @Data
    public static class BookingInfoDTO {
        // For hotels use confirmationNumber;
        // For activities use ticketType.
        private String confirmationNumber;
        private String reservationNumber;
        private String notes;
    }

    @Data
    public static class TravelInfoDTO {
        private String mode;
        private List<RouteDTO> routes;
    }

    @Data
    public static class RouteDTO {
        private String from;
        private String to;
        private Double distance;
        private Integer duration;
        private String polyline;
    }

    @Data
    public static class SharedWithDTO {
        private String userId;
        private String email;
        private String permission;
    }

    @Data
    public static class MetadataDTO {
        private List<String> tags;
        private Boolean isTemplate;
        private String language;
        private String lastSavedBy;
        private Integer version;
    }

}
