package com.eazybytes.controller;

import com.eazybytes.dto.CreateInventoryRequest;
import com.eazybytes.dto.UpdateInventoryRequest;
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

    @PostMapping("/phone")
    public ResponseEntity<PhoneInventory> createPhoneInventory(
            @Valid @RequestBody CreateInventoryRequest request) {
        PhoneInventory updatedInventory = inventoryService.createPhoneInventory(request);
        return ResponseEntity.ok(updatedInventory);
    }

    @PostMapping("/laptop")
    public ResponseEntity<LaptopInventory> createLaptopInventory(
            @Valid @RequestBody CreateInventoryRequest request) {
        LaptopInventory updatedInventory = inventoryService.createLaptopInventory(request);
        return ResponseEntity.ok(updatedInventory);
    }

    @PutMapping("/phone")
    public ResponseEntity<PhoneInventory> updatePhoneInventory(
            @Valid @RequestBody UpdateInventoryRequest request) {
        PhoneInventory updatedInventory = inventoryService.updatePhoneInventory(request);
        return ResponseEntity.ok(updatedInventory);
    }

    @PutMapping("/laptop")
    public ResponseEntity<LaptopInventory> updateLaptopInventory(
            @Valid @RequestBody UpdateInventoryRequest request) {
        LaptopInventory updatedInventory = inventoryService.updateLaptopInventory(request);
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

}