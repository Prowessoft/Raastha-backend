package com.aitravel.application.dto.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {
    private String name;
    private double latitude;
    private double longitude;
}


