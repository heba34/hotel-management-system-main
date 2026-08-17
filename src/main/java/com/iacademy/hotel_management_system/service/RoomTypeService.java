package com.iacademy.hotel_management_system.service;

import com.iacademy.hotel_management_system.dto.RoomTypeRequest;
import com.iacademy.hotel_management_system.dto.RoomTypeResponse;
import com.iacademy.hotel_management_system.entity.RoomType;
import com.iacademy.hotel_management_system.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public List<RoomTypeResponse> getAllRoomTypes() {

        return roomTypeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // تم التعديل إلى String
    public RoomTypeResponse getRoomTypeById(String id) {

        RoomType roomType = roomTypeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "RoomType not found with id: " + id
                        )
                );

        return mapToResponse(roomType);
    }

    public RoomTypeResponse saveRoomType(
            RoomTypeRequest request
    ) {

        if (roomTypeRepository.existsByName(
                request.getName())) {

            throw new RuntimeException(
                    "RoomType already exists: "
                            + request.getName()
            );
        }

        RoomType roomType = new RoomType();

        roomType.setName(request.getName());
        roomType.setDescription(request.getDescription());
        roomType.setBasePrice(request.getBasePrice());
        roomType.setMaxAdults(request.getMaxAdults());
        roomType.setMaxChildren(request.getMaxChildren());

        RoomType savedRoomType =
                roomTypeRepository.save(roomType);

        return mapToResponse(savedRoomType);
    }

    // تم التعديل إلى String
    public RoomTypeResponse updateRoomType(
            String id,
            RoomTypeRequest request
    ) {

        RoomType roomType = roomTypeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "RoomType not found with id: " + id
                        )
                );

        roomType.setName(request.getName());
        roomType.setDescription(request.getDescription());
        roomType.setBasePrice(request.getBasePrice());
        roomType.setMaxAdults(request.getMaxAdults());
        roomType.setMaxChildren(request.getMaxChildren());

        RoomType updatedRoomType =
                roomTypeRepository.save(roomType);

        return mapToResponse(updatedRoomType);
    }

    // تم التعديل إلى String
    public void deleteRoomType(String id) {

        if (!roomTypeRepository.existsById(id)) {
            throw new RuntimeException(
                    "RoomType not found with id: " + id
            );
        }

        roomTypeRepository.deleteById(id);
    }

    private RoomTypeResponse mapToResponse(
            RoomType roomType
    ) {

        return RoomTypeResponse.builder()
                .id(roomType.getId())
                .name(roomType.getName())
                .description(roomType.getDescription())
                .basePrice(roomType.getBasePrice())
                .maxAdults(roomType.getMaxAdults())
                .maxChildren(roomType.getMaxChildren())
                .build();
    }
}