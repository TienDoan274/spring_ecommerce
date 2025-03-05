package com.eazybytes.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryResponse {
    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    private String color;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Column(nullable = false)
    @Min(value = 0, message = "Price cannot be negative")
    private Integer originalPrice;

    @Column(nullable = false)
    @Min(value = 0, message = "Price cannot be negative")
    private Integer currentPrice;

}