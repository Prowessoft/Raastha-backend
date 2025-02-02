package com.aitravel.application.dto.requestdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsLetterRequest {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Please provide a valid email address")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must be in valid format"
    )
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;
}
