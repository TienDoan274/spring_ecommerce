package com.eazybytes.client;

import com.eazybytes.dto.GroupVariantsDto;
import com.eazybytes.dto.InventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service", url = "${inventory-service.url}")
public interface InventoryClient {

    @GetMapping("/api/inventory/product")
    ResponseEntity<InventoryDto> getProductInventory(@RequestParam String productId, @RequestParam(required = false) String color);

    @PostMapping("/api/inventory")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    ResponseEntity<InventoryDto> createProductInventory(@RequestBody InventoryDto request);

    @DeleteMapping("/api/inventory/delete/{productId}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    ResponseEntity<Void> deleteInventoriesByProductId(@PathVariable("productId") String productId);

    @PostMapping("/api/group-variants")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<GroupVariantsDto> createGroupVariants(@RequestBody GroupVariantsDto dto);

}