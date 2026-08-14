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

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {

        return ResponseEntity.ok(
                roomService.getAllRooms()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                roomService.getRoomById(id)
        );
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @RequestBody RoomRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomService.saveRoom(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequest request
    ) {

        return ResponseEntity.ok(
                roomService.updateRoom(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long id
    ) {

        roomService.deleteRoom(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomResponse>>
    getAvailableRooms() {

        return ResponseEntity.ok(
                roomService.getAvailableRooms()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RoomResponse>>
    getRoomsByStatus(
            @PathVariable RoomStatus status
    ) {

        return ResponseEntity.ok(
                roomService.getRoomsByStatus(status)
        );
    }

    @GetMapping("/type/{roomTypeId}")
    public ResponseEntity<List<RoomResponse>>
    getRoomsByType(
            @PathVariable Long roomTypeId
    ) {

        return ResponseEntity.ok(
                roomService.getRoomsByType(roomTypeId)
        );
    }
}