package com.aitravel.application.dto.requestdtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayRequest {
    @JsonProperty("day_number")
    private int dayNumber;
    private List<LocationRequest> locations;
}

