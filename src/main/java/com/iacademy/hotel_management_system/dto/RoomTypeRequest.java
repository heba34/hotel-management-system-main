package com.iacademy.hotel_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomTypeRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;

    @NotNull(message = "Max adults is required")
    @Positive(message = "Max adults must be positive")
    private Integer maxAdults;

    @NotNull(message = "Max children is required")
    @Positive(message = "Max children must be positive")
    private Integer maxChildren;
}