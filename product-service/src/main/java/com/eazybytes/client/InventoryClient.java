package com.eazybytes.client;

import com.eazybytes.dto.ProductInventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eazybytes.dto.CreateInventoryRequest;

@FeignClient(name = "inventory-service", url = "${inventory-service.url}")
public interface InventoryClient {
    @PostMapping("/api/inventory/product")
    ResponseEntity<ProductInventoryResponse> createProductInventory(@RequestBody CreateInventoryRequest request);

    @DeleteMapping("/api/inventory/delete/{productId}")
    ResponseEntity<Void> deleteInventoriesByProductId(@PathVariable("productId") String productId);
}