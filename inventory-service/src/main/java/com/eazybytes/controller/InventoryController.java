package com.eazybytes.controller;

import com.eazybytes.dto.GroupVariantsDto;
import com.eazybytes.dto.InventoryDto;
import com.eazybytes.exception.InventoryAlreadyExistsException;
import com.eazybytes.model.GroupVariants;
import com.eazybytes.model.ProductInventory;
import com.eazybytes.repository.GroupVariantsRepository;
import com.eazybytes.repository.ProductInventoryRepository;
import com.eazybytes.service.InventoryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductInventoryRepository productInventoryRepository;
    private final GroupVariantsRepository groupVariantsRepository;

    @GetMapping("/productColorVariants/{productId}")
    public ResponseEntity<List<InventoryDto>> getProductColorVariants(@PathVariable String productId) {
        List<InventoryDto> variants = inventoryService.findAllColorVariantsByProductId(productId);

        if (variants.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(variants);
    }

    @GetMapping("/product")
    public ResponseEntity<InventoryDto> getProductInventory(
            @RequestParam String productId,
            @RequestParam(required = false) String color) {
        ProductInventory inventory = inventoryService.getProductInventory(productId, color);

        InventoryDto inventoryDto = InventoryDto.builder()
                .productId(inventory.getProductId())
                .color(inventory.getColor())
                .quantity(inventory.getQuantity())
                .originalPrice(inventory.getOriginalPrice())
                .currentPrice(inventory.getCurrentPrice())
                .build();

        return ResponseEntity.ok(inventoryDto);
    }


    @PostMapping
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<InventoryDto> createInventory(@Valid @RequestBody InventoryDto request) {
        try {
            ProductInventory createdInventory = inventoryService.createProductInventory(request);

            InventoryDto inventoryDto = InventoryDto.builder()
                    .productId(createdInventory.getProductId())
                    .variant(createdInventory.getVariant())
                    .productName(createdInventory.getProductName())
                    .color(createdInventory.getColor())
                    .quantity(createdInventory.getQuantity())
                    .originalPrice(createdInventory.getOriginalPrice())
                    .currentPrice(createdInventory.getCurrentPrice())
                    .build();

            return new ResponseEntity<>(inventoryDto, HttpStatus.CREATED);
        } catch (InventoryAlreadyExistsException e) {
            log.error("Inventory already exists: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new InventoryDto()); // Or return a more descriptive error DTO
        } catch (Exception e) {
            log.error("Error creating inventory: {}", e.getMessage(), e);
            log.debug("Request data: {}", request);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InventoryDto()); // Or return a more descriptive error DTO
        }
    }

    @PutMapping("/product")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<InventoryDto> updateProductInventory(
            @Valid @RequestBody InventoryDto request) {
        ProductInventory updatedInventory = inventoryService.updateProductInventory(request);

        InventoryDto inventoryDto = InventoryDto.builder()
                .inventoryId(updatedInventory.getInventoryId())
                .productId(updatedInventory.getProductId())
                .color(updatedInventory.getColor())
                .variant(updatedInventory.getVariant())
                .quantity(updatedInventory.getQuantity())
                .originalPrice(updatedInventory.getOriginalPrice())
                .currentPrice(updatedInventory.getCurrentPrice())
                .build();

        return ResponseEntity.ok(inventoryDto);
    }

    @PostMapping("/decrease")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<InventoryDto> decreaseProductQuantity(
            @RequestParam String productId,
            @RequestParam String color,
            @RequestParam int quantity) {
        ProductInventory updatedInventory = inventoryService.decreaseProductQuantity(productId, color, quantity);

        InventoryDto inventoryDto = InventoryDto.builder()
                .productId(updatedInventory.getProductId())
                .color(updatedInventory.getColor())
                .quantity(updatedInventory.getQuantity())
                .originalPrice(updatedInventory.getOriginalPrice())
                .currentPrice(updatedInventory.getCurrentPrice())
                .build();

        return ResponseEntity.ok(inventoryDto);
    }

    /**
     * Tăng số lượng điện thoại
     */
    @PostMapping("/increase")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<InventoryDto> increaseProductQuantity(
            @RequestParam String productId,
            @RequestParam String color,
            @RequestParam int quantity) {
        ProductInventory updatedInventory = inventoryService.increaseProductQuantity(productId, color, quantity);

        InventoryDto inventoryDto = InventoryDto.builder()
                .inventoryId(updatedInventory.getInventoryId())
                .productId(updatedInventory.getProductId())
                .color(updatedInventory.getColor())
                .quantity(updatedInventory.getQuantity())
                .originalPrice(updatedInventory.getOriginalPrice())
                .currentPrice(updatedInventory.getCurrentPrice())
                .build();

        return ResponseEntity.ok(inventoryDto);
    }


    @DeleteMapping("/delete/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInventoriesByProductId(@PathVariable("productId") String productId) {
        inventoryService.deleteAllByProductId(productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductInventory(@PathParam("productId") String productId, @PathParam("color") String color)
    {
        inventoryService.deleteProductInventory(productId,color);
        return ResponseEntity.noContent().build();
    }
}