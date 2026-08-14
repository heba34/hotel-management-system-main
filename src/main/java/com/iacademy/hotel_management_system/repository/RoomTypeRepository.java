package com.iacademy.hotel_management_system.repository;

import com.iacademy.hotel_management_system.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    boolean existsByName(String name);
}