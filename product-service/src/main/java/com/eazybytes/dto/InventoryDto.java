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
    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    private String color;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 0, message = "Price cannot be negative")
    private Integer originalPrice;

    @Min(value = 0, message = "Price cannot be negative")
    private Integer currentPrice;

    private Integer priorityNumber;

}