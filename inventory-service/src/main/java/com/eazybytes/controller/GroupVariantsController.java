package com.eazybytes.controller;

import com.eazybytes.dto.GroupWithProductsDto;
import com.eazybytes.dto.InventoryDto;
import com.eazybytes.model.GroupVariants;
import com.eazybytes.service.GroupVariantsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group-variants")
@Slf4j
public class GroupVariantsController {

    @Autowired
    private GroupVariantsService groupVariantsService;

    @GetMapping("/groups")
    public ResponseEntity<?> getAllProductsByGroup(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // Tạo PageRequest với size là số lượng groups muốn lấy
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "priorityNumber"));
            List<GroupWithProductsDto> response = groupVariantsService.getAllProductsByGroup(pageRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 500);
            errorResponse.put("code", "INTERNAL_SERVER_ERROR");
            errorResponse.put("message", "Error processing request: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> createGroup(@RequestBody Map<String, Object> request) {
        List<String> productIds = (List<String>) request.get("productIds");
        Integer priorityNumber = request.get("priorityNumber") != null ?
                Integer.parseInt(request.get("priorityNumber").toString()) : null;
        String image = (String) request.get("image");
        Long groupId = groupVariantsService.createGroupAndAssignProducts(productIds, priorityNumber,image);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("groupId", groupId));
    }

    /**
     * API endpoint để lấy tất cả các sản phẩm trong một nhóm
     */
    @GetMapping("/{groupId}/products")
    public ResponseEntity<List<InventoryDto>> getProductsByGroup(@PathVariable Long groupId) {
        List<InventoryDto> products = groupVariantsService.getProductsByGroupId(groupId);

        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(products);
    }

    /**
     * API endpoint để xóa một nhóm
     */
    @DeleteMapping("/{groupId}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groupVariantsService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    /**
     * API endpoint để cập nhật độ ưu tiên của một nhóm
     */
    @PutMapping("/{groupId}/priority")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<GroupVariants> updateGroupPriority(
            @PathVariable Long groupId,
            @RequestBody Map<String, Integer> request) {

        Integer priorityNumber = request.get("priorityNumber");

        if (priorityNumber == null) {
            return ResponseEntity.badRequest().build();
        }

        GroupVariants updatedGroup = groupVariantsService.updateGroupPriority(groupId, priorityNumber);
        return ResponseEntity.ok(updatedGroup);
    }

    /**
     * API endpoint để thêm sản phẩm vào một nhóm
     */
    @PostMapping("/{groupId}/products")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<List<InventoryDto>> addProductsToGroup(
            @PathVariable Long groupId,
            @RequestBody Map<String, List<String>> request) {

        List<String> productIds = request.get("productIds");

        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<InventoryDto> updatedInventories = groupVariantsService.addProductsToGroup(groupId, productIds);
        return ResponseEntity.ok(updatedInventories);
    }

    /**
     * API endpoint để xóa sản phẩm khỏi một nhóm
     */
    @DeleteMapping("/{groupId}/products")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<Void> removeProductsFromGroup(
            @PathVariable Long groupId,
            @RequestBody Map<String, List<String>> request) {

        List<String> productIds = request.get("productIds");

        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        groupVariantsService.removeProductsFromGroup(groupId, productIds);
        return ResponseEntity.noContent().build();
    }
}