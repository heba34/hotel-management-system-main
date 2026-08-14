package com.iacademy.hotel_management_system.controller;

import com.iacademy.hotel_management_system.dto.RoomTypeRequest;
import com.iacademy.hotel_management_system.dto.RoomTypeResponse;
import com.iacademy.hotel_management_system.service.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private RoomTypeRequest request;

    @GetMapping
    public ResponseEntity<List<RoomTypeResponse>>
    getAllRoomTypes() {

        return ResponseEntity.ok(
                roomTypeService.getAllRoomTypes()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeResponse>
    getRoomTypeById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                roomTypeService.getRoomTypeById(id)
        );
    }

    @PostMapping
    public ResponseEntity<RoomTypeResponse>
    createRoomType(
            @Valid @RequestBody RoomTypeRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomTypeService.saveRoomType(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeResponse>
    updateRoomType(
            @PathVariable Long id,
            @Valid @RequestBody RoomTypeRequest request
    ) {
        this.request = request;

        return ResponseEntity.ok(
                roomTypeService.updateRoomType(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(
            @PathVariable Long id
    ) {

        roomTypeService.deleteRoomType(id);

        return ResponseEntity.noContent().build();
    }
}