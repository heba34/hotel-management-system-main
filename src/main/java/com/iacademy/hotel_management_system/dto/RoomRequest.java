package com.iacademy.hotel_management_system.dto;

import com.iacademy.hotel_management_system.enums.RoomStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomRequest {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotBlank(message = "Floor is required")
    private String floor;

    private RoomStatus status;

    @NotNull(message = "Room type id is required")
    private Long roomTypeId;
}