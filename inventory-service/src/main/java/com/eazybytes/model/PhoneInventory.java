package com.eazybytes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "phone_inventory",
        indexes = {
                @Index(name = "idx_phone_variant", columnList = "phone_id, color", unique = true)
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_id", nullable = false)
    private String phoneId;  // ID tham chiếu đến Phone trong service product

    @Column(nullable = false)
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Column(nullable = false)
    private String color;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}