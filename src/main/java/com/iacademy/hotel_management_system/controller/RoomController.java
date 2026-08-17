package com.iacademy.hotel_management_system.controller;

import com.iacademy.hotel_management_system.dto.RoomRequest;
import com.iacademy.hotel_management_system.dto.RoomResponse;
import com.iacademy.hotel_management_system.enums.RoomStatus;
import com.iacademy.hotel_management_system.service.RoomService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;


    // =========================
    // 3.1 + 3.2
    // Search Available Rooms by Date
    // + Filter by Type / Price / Occupancy
    // =========================

    @GetMapping("/search")
    public ResponseEntity<List<RoomResponse>> searchAvailableRooms(

            @RequestParam LocalDate checkIn,

            @RequestParam LocalDate checkOut,

            @RequestParam(required = false)
            String type,

            @RequestParam(required = false)
            BigDecimal minPrice,

            @RequestParam(required = false)
            BigDecimal maxPrice,

            @RequestParam(required = false)
            Integer maxOccupancy
    ) {

        return ResponseEntity.ok(
                roomService.searchAvailableRooms(
                        checkIn,
                        checkOut,
                        type,
                        minPrice,
                        maxPrice,
                        maxOccupancy
                )
        );
    }


    // =========================
    // 2.4 List & View Rooms
    // =========================

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {

        return ResponseEntity.ok(
                roomService.getAllRooms()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(
            @PathVariable String id // تم التعديل إلى String
    ) {

        return ResponseEntity.ok(
                roomService.getRoomById(id)
        );
    }


    // =========================
    // 2.1 Create Room
    // =========================

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @Valid @RequestBody RoomRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomService.saveRoom(request));
    }


    // =========================
    // 2.2 Update Room
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable String id, // تم التعديل إلى String
            @Valid @RequestBody RoomRequest request
    ) {

        return ResponseEntity.ok(
                roomService.updateRoom(id, request)
        );
    }


    // =========================
    // 2.3 Delete Room
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable String id // تم التعديل إلى String
    ) {

        roomService.deleteRoom(id);

        return ResponseEntity.noContent().build();
    }


    // =========================
    // Get Available Rooms
    // =========================

    @GetMapping("/available")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms() {

        return ResponseEntity.ok(
                roomService.getAvailableRooms()
        );
    }


    // =========================
    // Filter by Status
    // =========================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RoomResponse>> getRoomsByStatus(
            @PathVariable RoomStatus status
    ) {

        return ResponseEntity.ok(
                roomService.getRoomsByStatus(status)
        );
    }


    // =========================
    // Filter by Room Type
    // =========================

    @GetMapping("/type/{roomTypeId}")
    public ResponseEntity<List<RoomResponse>> getRoomsByType(
            @PathVariable String roomTypeId // تم التعديل إلى String
    ) {

        return ResponseEntity.ok(
                roomService.getRoomsByType(roomTypeId)
        );
    }
}