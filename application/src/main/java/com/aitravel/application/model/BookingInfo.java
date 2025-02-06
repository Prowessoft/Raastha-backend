package com.aitravel.application.model;

import lombok.Data;
import jakarta.persistence.Embeddable;


@Data
@Embeddable
public class BookingInfo {
    // For hotels use confirmationNumber; for activities use ticketType, etc.
    private String confirmationNumber;
    private String reservationNumber;
    private String notes;
}