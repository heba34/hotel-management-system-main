package com.iacademy.hotel_management_system.service;

import com.iacademy.hotel_management_system.dto.RoomRequest;
import com.iacademy.hotel_management_system.dto.RoomResponse;
import com.iacademy.hotel_management_system.entity.Room;
import com.iacademy.hotel_management_system.entity.RoomType;
import com.iacademy.hotel_management_system.enums.RoomStatus;
import com.iacademy.hotel_management_system.repository.RoomRepository;
import com.iacademy.hotel_management_system.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    // =========================
    // Get All Rooms
    // =========================

    public List<RoomResponse> getAllRooms() {

        return roomRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // Get Room By ID
    // =========================

    public RoomResponse getRoomById(String id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Room not found with id: " + id
                        )
                );

        return mapToResponse(room);
    }

    // =========================
    // Create Room
    // =========================

    public RoomResponse saveRoom(RoomRequest request) {

        if (roomRepository.existsByRoomNumber(
                request.getRoomNumber())) {

            throw new RuntimeException(
                    "Room number already exists: "
                            + request.getRoomNumber()
            );
        }

        RoomType roomType = roomTypeRepository
                .findById(request.getRoomTypeId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "RoomType not found with id: "
                                        + request.getRoomTypeId()
                        )
                );

        Room room = new Room();

        room.setRoomNumber(request.getRoomNumber());
        room.setFloor(request.getFloor());

        // MongoDB stores the RoomType ID
        room.setRoomTypeId(roomType.getId());

        if (request.getStatus() == null) {
            room.setStatus(RoomStatus.AVAILABLE);
        } else {
            room.setStatus(request.getStatus());
        }

        Room savedRoom = roomRepository.save(room);

        return mapToResponse(savedRoom);
    }

    // =========================
    // Update Room
    // =========================

    public RoomResponse updateRoom(
            String id,
            RoomRequest request
    ) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Room not found with id: " + id
                        )
                );

        RoomType roomType = roomTypeRepository
                .findById(request.getRoomTypeId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "RoomType not found with id: "
                                        + request.getRoomTypeId()
                        )
                );

        room.setRoomNumber(request.getRoomNumber());
        room.setFloor(request.getFloor());
        room.setRoomTypeId(roomType.getId());

        if (request.getStatus() != null) {
            room.setStatus(request.getStatus());
        }

        Room updatedRoom = roomRepository.save(room);

        return mapToResponse(updatedRoom);
    }

    // =========================
    // Delete Room
    // =========================

    public void deleteRoom(String id) {

        if (!roomRepository.existsById(id)) {

            throw new RuntimeException(
                    "Room not found with id: " + id
            );
        }

        roomRepository.deleteById(id);
    }

    // =========================
    // Get Available Rooms
    // =========================

    public List<RoomResponse> getAvailableRooms() {

        return roomRepository
                .findByStatus(RoomStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // Search Available Rooms
    // =========================

    public List<RoomResponse> searchAvailableRooms(
            LocalDate checkIn,
            LocalDate checkOut,
            String type,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer maxOccupancy
    ) {

        // Check dates
        if (checkIn == null || checkOut == null) {

            throw new IllegalArgumentException(
                    "Check-in and check-out dates are required"
            );
        }

        // Check-in must be today or future
        if (checkIn.isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Check-in date cannot be in the past"
            );
        }

        // Check-out must be after check-in
        if (!checkOut.isAfter(checkIn)) {

            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }

        return roomRepository
                .findByStatus(RoomStatus.AVAILABLE)
                .stream()

                // =========================
                // Get RoomType
                // =========================

                .filter(room ->
                        room.getRoomTypeId() != null
                )

                // =========================
                // Filter by Type
                // =========================

                .filter(room -> {

                    if (type == null || type.isBlank()) {
                        return true;
                    }

                    RoomType roomType =
                            roomTypeRepository
                                    .findById(room.getRoomTypeId())
                                    .orElse(null);

                    return roomType != null
                            && roomType.getName()
                            .equalsIgnoreCase(type);
                })

                // =========================
                // Filter by Min Price
                // =========================

                .filter(room -> {

                    if (minPrice == null) {
                        return true;
                    }

                    RoomType roomType =
                            roomTypeRepository
                                    .findById(room.getRoomTypeId())
                                    .orElse(null);

                    return roomType != null
                            && roomType.getBasePrice()
                            .compareTo(minPrice) >= 0;
                })

                // =========================
                // Filter by Max Price
                // =========================

                .filter(room -> {

                    if (maxPrice == null) {
                        return true;
                    }

                    RoomType roomType =
                            roomTypeRepository
                                    .findById(room.getRoomTypeId())
                                    .orElse(null);

                    return roomType != null
                            && roomType.getBasePrice()
                            .compareTo(maxPrice) <= 0;
                })

                // =========================
                // Filter by Occupancy
                // =========================

                .filter(room -> {

                    if (maxOccupancy == null) {
                        return true;
                    }

                    RoomType roomType =
                            roomTypeRepository
                                    .findById(room.getRoomTypeId())
                                    .orElse(null);

                    if (roomType == null) {
                        return false;
                    }

                    int totalOccupancy =
                            roomType.getMaxAdults()
                                    + roomType.getMaxChildren();

                    return totalOccupancy <= maxOccupancy;
                })

                // =========================
                // Sort by Price Ascending
                // =========================

                .sorted((room1, room2) -> {

                    RoomType type1 =
                            roomTypeRepository
                                    .findById(room1.getRoomTypeId())
                                    .orElse(null);

                    RoomType type2 =
                            roomTypeRepository
                                    .findById(room2.getRoomTypeId())
                                    .orElse(null);

                    if (type1 == null || type2 == null) {
                        return 0;
                    }

                    return type1.getBasePrice()
                            .compareTo(type2.getBasePrice());
                })

                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // Get Rooms By Status
    // =========================

    public List<RoomResponse> getRoomsByStatus(
            RoomStatus status
    ) {

        return roomRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // Get Rooms By Type
    // =========================

    public List<RoomResponse> getRoomsByType(
            String roomTypeId
    ) {

        if (!roomTypeRepository.existsById(roomTypeId)) {

            throw new RuntimeException(
                    "RoomType not found with id: " + roomTypeId
            );
        }

        return roomRepository
                .findByRoomTypeId(roomTypeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // Map Room To Response
    // =========================

    private RoomResponse mapToResponse(Room room) {

        RoomType roomType =
                roomTypeRepository
                        .findById(room.getRoomTypeId())
                        .orElse(null);

        return RoomResponse.builder()
                .id(room.getId())
                .roomTypeId(
                        roomType != null
                                ? roomType.getId()
                                : null
                )
                .roomTypeName(
                        roomType != null
                                ? roomType.getName()
                                : null
                )
                .roomNumber(room.getRoomNumber())
                .floor(room.getFloor())
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
}