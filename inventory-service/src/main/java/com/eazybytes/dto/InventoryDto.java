package com.eazybytes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InventoryDto {
    private Long inventoryId;

    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    private String color;

    private Integer quantity;

    private String originalPrice;

    private String currentPrice;

}