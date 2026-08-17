package com.iacademy.hotel_management_system.repository;

import com.iacademy.hotel_management_system.entity.RoomType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends MongoRepository<RoomType, String> {

    // الدالة دي اللي كانت ناقصة وعاملة مشكلة في الـ Service
    boolean existsByName(String name);

}