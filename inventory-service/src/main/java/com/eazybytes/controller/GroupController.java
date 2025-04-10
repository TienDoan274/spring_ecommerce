package com.eazybytes.controller;

import com.eazybytes.dto.GroupProductDto;
import com.eazybytes.dto.GroupWithProductsDto;
import com.eazybytes.dto.InventoryDto;
import com.eazybytes.model.Group;
import com.eazybytes.repository.GroupRepository;
import com.eazybytes.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/group-variants")
@Slf4j
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @GetMapping("/search")
    public ResponseEntity<List<GroupProductDto>> searchProducts(
            @RequestParam("query") String query
    ) {
        List<GroupProductDto> results = groupService.searchProducts(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getAllProductsByGroup(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = true) String type,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false, defaultValue = "asc") String sortByPrice,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        try {
            List<String> tagList = (tags != null && !tags.isEmpty()) ? Arrays.asList(tags.split(",")) : null;

            List<GroupWithProductsDto> content = groupService.getAllProductsByGroup(page, size, type, tagList, sortByPrice, minPrice, maxPrice);

            long totalElements = groupService.countGroupsByTypeAndTags(type, tagList);
            int totalPages = (int) Math.ceil((double) totalElements / size);

            Map<String, Object> response = new HashMap<>();
            response.put("content", content);
            response.put("totalPages", totalPages);
            response.put("totalElements", totalElements);
            response.put("currentPage", page);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<Map<String, Integer>> createGroup(@RequestBody Map<String, Object> request) {
        log.debug("Received request to create group: {}", request);

        List<String> productIds = (List<String>) request.get("productIds");
        log.debug("Extracted productIds: {}", productIds);

        List<String> variants = (List<String>) request.get("variants");

        List<String> productNames = (List<String>) request.get("productNames");

        List<Integer> defaultOriginalPrices = (List<Integer>) request.get("defaultOriginalPrices");

        List<Integer> defaultCurrentPrices = (List<Integer>) request.get("defaultCurrentPrices");
        Integer orderNumber = request.get("orderNumber") != null ?
                Integer.parseInt(request.get("orderNumber").toString()) : null;
        log.debug("Extracted orderNumber: {}", orderNumber);

        String image = (String) request.get("image");
        log.debug("Extracted image: {}", image);

        String type = (String) request.get("type");
        log.debug("Extracted type: {}", type);

        log.debug("Calling groupService.createGroupAndAssignProducts");
        Integer groupId = groupService.createGroupAndAssignProducts(productIds, orderNumber, image, type,variants,productNames,defaultOriginalPrices,defaultCurrentPrices);
        log.debug("Created group with ID: {}", groupId);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("groupId", groupId));
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<?> updateGroup(@PathVariable Integer groupId, @RequestBody Map<String, Object> request) {
        log.debug("Received request to update group with ID {}: {}", groupId, request);

        try {
            // Check if group exists
            Optional<Group> existingGroup = groupRepository.findById(groupId);
            if (existingGroup.isEmpty()) {
                log.warn("Group with ID {} not found", groupId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Group not found"));
            }

            List<String> productIds = (List<String>) request.get("productIds");
            List<String> variants = (List<String>) request.get("variants");
            List<String> productNames = (List<String>) request.get("productNames");

            List<Integer> defaultOriginalPrices = (List<Integer>) request.get("defaultOriginalPrices");
            List<Integer> defaultCurrentPrices = (List<Integer>) request.get("defaultCurrentPrices");

            Integer orderNumber = request.get("orderNumber") != null ?
                    Integer.parseInt(request.get("orderNumber").toString()) : null;

            String image = (String) request.get("image");
            String type = (String) request.get("type");

            log.debug("Calling groupService.updateGroupAndProducts");
            groupService.updateGroupAndProducts(groupId, productIds, variants,productNames, orderNumber,
                    image, type, defaultOriginalPrices, defaultCurrentPrices);

            return ResponseEntity.ok(Map.of("message", "Group updated successfully"));
        } catch (Exception e) {
            log.error("Error updating group: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error updating group: " + e.getMessage()));
        }
    }

    @PutMapping("/{groupId}/priority")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<Group> updateGroupPriority(
            @PathVariable Integer groupId,
            @RequestBody Map<String, Integer> request) {

        Integer orderNumber = request.get("orderNumber");

        if (orderNumber == null) {
            return ResponseEntity.badRequest().build();
        }

        Group updatedGroup = groupService.updateGroupPriority(groupId, orderNumber);
        return ResponseEntity.ok(updatedGroup);
    }

}