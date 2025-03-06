package com.eazybytes.controller;

import com.eazybytes.dto.GroupVariantsDto;
import com.eazybytes.service.GroupVariantsService;
import jakarta.persistence.EntityNotFoundException;
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

@RestController
@RequestMapping("/api/group-variants")
@RequiredArgsConstructor
@Slf4j
public class GroupVariantsController {

    private final GroupVariantsService groupVariantsService;

    /**
     * Tạo nhóm sản phẩm mới
     */
    @PostMapping
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<GroupVariantsDto> createGroupVariants(@RequestBody GroupVariantsDto dto) {
        log.info("Request to create group variants with {} products",
                dto.getInventoryIds() != null ? dto.getInventoryIds().size() : 0);

        GroupVariantsDto createdDto = groupVariantsService.createGroupVariants(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    /**
     * Lấy thông tin nhóm theo ID
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupVariantsDto> getGroupVariantsById(@PathVariable Long groupId) {
        try {
            GroupVariantsDto dto = groupVariantsService.getGroupVariantsById(groupId);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            log.error("GroupVariants not found with ID: {}", groupId);
            return ResponseEntity.notFound().build();
        }
    }



    /**
     * Lấy danh sách tất cả các nhóm với phân trang và sắp xếp
     */
    @GetMapping
    public ResponseEntity<Page<GroupVariantsDto>> getAllGroupVariants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "priorityNumber") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<GroupVariantsDto> result = groupVariantsService.getAllGroupVariants(pageable);

        return ResponseEntity.ok(result);
    }

    /**
     * Cập nhật thông tin nhóm
     */
    @PutMapping("/{groupId}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<GroupVariantsDto> updateGroupVariants(
            @PathVariable Long groupId,
            @RequestBody GroupVariantsDto dto) {

        try {
            GroupVariantsDto updatedDto = groupVariantsService.updateGroupVariants(groupId, dto);
            return ResponseEntity.ok(updatedDto);
        } catch (EntityNotFoundException e) {
            log.error("Failed to update. GroupVariants not found with ID: {}", groupId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Xóa nhóm theo ID
     */
    @DeleteMapping("/{groupId}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGroupVariants(@PathVariable Long groupId) {
        try {
            groupVariantsService.deleteGroupVariants(groupId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error("Failed to delete. GroupVariants not found with ID: {}", groupId);
            return ResponseEntity.notFound().build();
        }
    }
}