package com.aitravel.application.dto.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryResponse {
    private String userId;
    private String title;
    private String description;
    private List<DayResponse> days;
}

