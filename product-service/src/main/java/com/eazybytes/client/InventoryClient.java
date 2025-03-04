package com.eazybytes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eazybytes.dto.CreateInventoryRequest;
import com.eazybytes.dto.PhoneInventoryResponse;

@FeignClient(name = "inventory-service", url = "${inventory-service.url}")
public interface InventoryClient {
    @PostMapping("/api/inventory/phone")
    ResponseEntity<PhoneInventoryResponse> createPhoneQuantity(@RequestBody CreateInventoryRequest request);

    @DeleteMapping("/api/inventory/phone/product/{productId}")
    ResponseEntity<Void> deleteInventoriesByProductId(@PathVariable("productId") String productId);
}