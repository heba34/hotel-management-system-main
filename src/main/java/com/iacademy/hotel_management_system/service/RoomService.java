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

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    public List<RoomResponse> getAllRooms() {

        return roomRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RoomResponse getRoomById(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Room not found with id: " + id
                        )
                );

        return mapToResponse(room);
    }

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
        room.setRoomType(roomType);

        if (request.getStatus() == null) {
            room.setStatus(RoomStatus.AVAILABLE);
        } else {
            room.setStatus(request.getStatus());
        }

        Room savedRoom = roomRepository.save(room);

        return mapToResponse(savedRoom);
    }

    public RoomResponse updateRoom(
            Long id,
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
        room.setRoomType(roomType);

        if (request.getStatus() != null) {
            room.setStatus(request.getStatus());
        }

        Room updatedRoom = roomRepository.save(room);

        return mapToResponse(updatedRoom);
    }

    public void deleteRoom(Long id) {

        if (!roomRepository.existsById(id)) {
            throw new RuntimeException(
                    "Room not found with id: " + id
            );
        }

        roomRepository.deleteById(id);
    }

    public List<RoomResponse> getAvailableRooms() {

        return roomRepository
                .findByStatus(RoomStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<RoomResponse> getRoomsByStatus(
            RoomStatus status
    ) {

        return roomRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<RoomResponse> getRoomsByType(
            Long roomTypeId
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

    private RoomResponse mapToResponse(Room room) {

        return RoomResponse.builder()
                .id(room.getId())
                .roomTypeId(room.getRoomType().getId())
                .roomTypeName(room.getRoomType().getName())
                .roomNumber(room.getRoomNumber())
                .floor(room.getFloor())
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
}