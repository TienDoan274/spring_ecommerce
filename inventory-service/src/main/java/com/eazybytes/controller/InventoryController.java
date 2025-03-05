package com.eazybytes.controller;

import com.eazybytes.dto.CreateInventoryRequest;
import com.eazybytes.dto.UpdateInventoryRequest;
import com.eazybytes.model.ProductInventory;
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

    @PostMapping("/product")
    public ResponseEntity<ProductInventory> createProductInventory(
            @Valid @RequestBody CreateInventoryRequest request) {
        ProductInventory updatedInventory = inventoryService.createProductInventory(request);
        return ResponseEntity.ok(updatedInventory);
    }

    @PutMapping("/product")
    public ResponseEntity<ProductInventory> updateProductInventory(
            @Valid @RequestBody UpdateInventoryRequest request) {
        ProductInventory updatedInventory = inventoryService.updateProductInventory(request);
        return ResponseEntity.ok(updatedInventory);
    }

    @PostMapping("/decrease")
    public ResponseEntity<ProductInventory> decreaseProductQuantity(
            @RequestParam String ProductId,
            @RequestParam String color,
            @RequestParam int quantity) {
        ProductInventory updatedInventory = inventoryService.decreaseProductQuantity(ProductId, color, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    /**
     * Tăng số lượng điện thoại
     */
    @PostMapping("/increase")
    public ResponseEntity<ProductInventory> increaseProductQuantity(
            @RequestParam String ProductId,
            @RequestParam String color,
            @RequestParam int quantity) {
        ProductInventory updatedInventory = inventoryService.increaseProductQuantity(ProductId, color, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    @DeleteMapping("/delete/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteInventoriesByProductId(@PathVariable("productId") String productId) {
        inventoryService.deleteAllByProductId(productId);
        return ResponseEntity.noContent().build();
    }

}