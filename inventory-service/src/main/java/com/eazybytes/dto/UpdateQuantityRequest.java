package com.eazybytes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuantityRequest {
    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @NotBlank(message = "Color cannot be blank")
    private String color;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}