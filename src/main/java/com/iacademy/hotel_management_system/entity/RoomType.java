package com.iacademy.hotel_management_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal; // لازم نضيف ده
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "room_types")
public class RoomType {

    @Id
    private String id;

    private String name;

    private String description;

    private BigDecimal basePrice; // تم التعديل لـ BigDecimal

    private Integer capacity;

    // تم إضافة المتغيرات الناقصة
    private Integer maxAdults;

    private Integer maxChildren;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Long createdBy;

    private Long updatedBy;
}