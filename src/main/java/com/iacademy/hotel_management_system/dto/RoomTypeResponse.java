package com.iacademy.hotel_management_system.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RoomTypeResponse {

    private String id;

    private String name;

    private String description;

    private BigDecimal basePrice;

    private Integer maxAdults;

    private Integer maxChildren;
}