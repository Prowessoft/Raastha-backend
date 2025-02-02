package com.aitravel.application.dto.requestdtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryRequest {
    @JsonProperty("user_id")
    private Long userId;
    private String title;
    private String description;
    private List<DayRequest> days;
}

