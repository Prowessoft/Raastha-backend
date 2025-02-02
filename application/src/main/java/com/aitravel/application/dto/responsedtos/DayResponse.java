package com.aitravel.application.dto.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayResponse {
    private int dayNumber;
    private List<LocationResponse> locations;
}

