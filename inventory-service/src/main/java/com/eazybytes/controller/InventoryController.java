package com.eazybytes.controller;

import com.eazybytes.dto.UpdateQuantityRequest;
import com.eazybytes.exception.InventoryNotFoundException;
import com.eazybytes.model.PhoneInventory;
import com.eazybytes.model.LaptopInventory;
import com.eazybytes.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Cập nhật số lượng điện thoại
     */
    @PutMapping("/phone")
    public ResponseEntity<PhoneInventory> updatePhoneQuantity(
            @Valid @RequestBody UpdateQuantityRequest request) {
        PhoneInventory updatedInventory = inventoryService.updatePhoneQuantity(request);
        return ResponseEntity.ok(updatedInventory);
    }

    /**
     * Cập nhật số lượng laptop
     */
    @PutMapping("/laptop")
    public ResponseEntity<LaptopInventory> updateLaptopQuantity(
            @Valid @RequestBody UpdateQuantityRequest request) {
        LaptopInventory updatedInventory = inventoryService.updateLaptopQuantity(request);
        return ResponseEntity.ok(updatedInventory);
    }

    /**
     * Giảm số lượng điện thoại
     */
    @PostMapping("/phone/decrease")
    public ResponseEntity<PhoneInventory> decreasePhoneQuantity(
            @RequestParam String phoneId,
            @RequestParam String color,
            @RequestParam int quantity) {
        PhoneInventory updatedInventory = inventoryService.decreasePhoneQuantity(phoneId, color, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    /**
     * Giảm số lượng laptop
     */
    @PostMapping("/laptop/decrease")
    public ResponseEntity<LaptopInventory> decreaseLaptopQuantity(
            @RequestParam String laptopId,
            @RequestParam String color,
            @RequestParam int quantity) {
        LaptopInventory updatedInventory = inventoryService.decreaseLaptopQuantity(laptopId, color, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    /**
     * Tăng số lượng điện thoại
     */
    @PostMapping("/phone/increase")
    public ResponseEntity<PhoneInventory> increasePhoneQuantity(
            @RequestParam String phoneId,
            @RequestParam String color,
            @RequestParam int quantity) {
        PhoneInventory updatedInventory = inventoryService.increasePhoneQuantity(phoneId, color, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    /**
     * Tăng số lượng laptop
     */
    @PostMapping("/laptop/increase")
    public ResponseEntity<LaptopInventory> increaseLaptopQuantity(
            @RequestParam String laptopId,
            @RequestParam String color,
            @RequestParam int quantity) {
        LaptopInventory updatedInventory = inventoryService.increaseLaptopQuantity(laptopId, color, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    /**
     * Exception handler
     */
    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<String> handleInventoryNotFoundException(InventoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}