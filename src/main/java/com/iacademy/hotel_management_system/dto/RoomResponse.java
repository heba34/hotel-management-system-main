package com.iacademy.hotel_management_system.dto;

import com.iacademy.hotel_management_system.enums.RoomStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RoomResponse {

    private String id; // تم التعديل إلى String

    private String roomTypeId; // تم التعديل إلى String

    private String roomTypeName;

    private String roomNumber;

    private String floor;

    private RoomStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}