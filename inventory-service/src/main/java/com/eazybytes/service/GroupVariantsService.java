package com.eazybytes.service;

import com.eazybytes.dto.GroupProductDto;
import com.eazybytes.dto.GroupVariantsDto;
import com.eazybytes.dto.GroupWithProductsDto;
import com.eazybytes.dto.InventoryDto;
import com.eazybytes.model.GroupVariants;
import com.eazybytes.model.ProductInventory;
import com.eazybytes.repository.GroupVariantsRepository;
import com.eazybytes.repository.ProductInventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupVariantsService {

    @Autowired
    private GroupVariantsRepository groupVariantsRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Transactional
    public Long createGroupAndAssignProducts(List<String> productIds, Integer priorityNumber,String image) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs list cannot be empty");
        }

        if (priorityNumber == null) {
            Integer highestPriority = groupVariantsRepository.findHighestPriorityNumber();
            priorityNumber = (highestPriority != null) ? highestPriority + 1 : 1;
        }

        GroupVariants newGroup = GroupVariants.builder()
                .image(image)
                .priorityNumber(priorityNumber)
                .build();

        GroupVariants savedGroup = groupVariantsRepository.save(newGroup);
        Long groupId = savedGroup.getGroupId();

        updateInventoriesWithGroupId(productIds, groupId);

        return groupId;
    }

    /**
     * Cập nhật groupId cho tất cả các bản ghi inventory của các sản phẩm
     *
     * @param productIds Danh sách các product ID
     * @param groupId ID của nhóm
     */
    private void updateInventoriesWithGroupId(List<String> productIds, Long groupId) {
        // Lấy tất cả inventory cho các productId
        List<ProductInventory> inventories = productInventoryRepository.findAllByProductIdIn(productIds);

        // Cập nhật groupId cho mỗi bản ghi
        inventories.forEach(inventory -> inventory.setGroupId(groupId));

        // Lưu các bản ghi đã cập nhật
        productInventoryRepository.saveAll(inventories);
    }

    /**
     * Lấy tất cả các sản phẩm trong cùng một nhóm
     *
     * @param groupId ID của nhóm
     * @return Danh sách các InventoryDto thuộc cùng một nhóm
     */
    public List<InventoryDto> getProductsByGroupId(Long groupId) {
        List<ProductInventory> inventories = productInventoryRepository.findAllByGroupId(groupId);
        return inventories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Xóa một nhóm và xóa các liên kết groupId trong inventory
     *
     * @param groupId ID của nhóm cần xóa
     */
    @Transactional
    public void deleteGroup(Long groupId) {
        // Lấy tất cả inventory có groupId này
        List<ProductInventory> inventories = productInventoryRepository.findAllByGroupId(groupId);

        // Xóa groupId khỏi các bản ghi inventory
        inventories.forEach(inventory -> inventory.setGroupId(null));
        productInventoryRepository.saveAll(inventories);

        // Xóa nhóm
        groupVariantsRepository.deleteById(groupId);
    }

    /**
     * Cập nhật độ ưu tiên của một nhóm
     *
     * @param groupId ID của nhóm
     * @param priorityNumber Số ưu tiên mới
     * @return Nhóm đã được cập nhật
     */
    public GroupVariants updateGroupPriority(Long groupId, Integer priorityNumber) {
        GroupVariants group = groupVariantsRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

        group.setPriorityNumber(priorityNumber);
        return groupVariantsRepository.save(group);
    }

    /**
     * Thêm sản phẩm vào một nhóm đã tồn tại
     *
     * @param groupId ID của nhóm
     * @param productIds Danh sách các product ID cần thêm vào nhóm
     * @return Danh sách các InventoryDto đã được cập nhật
     */
    @Transactional
    public List<InventoryDto> addProductsToGroup(Long groupId, List<String> productIds) {
        // Kiểm tra nhóm có tồn tại
        GroupVariants group = groupVariantsRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

        // Cập nhật groupId cho các inventory mới
        updateInventoriesWithGroupId(productIds, groupId);

        // Trả về danh sách đã cập nhật
        List<ProductInventory> updatedInventories = productInventoryRepository.findAllByProductIdIn(productIds);
        return updatedInventories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Xóa sản phẩm khỏi một nhóm
     *
     * @param groupId ID của nhóm
     * @param productIds Danh sách các product ID cần xóa khỏi nhóm
     */
    @Transactional
    public void removeProductsFromGroup(Long groupId, List<String> productIds) {
        // Lấy tất cả inventory của các sản phẩm trong nhóm này
        List<ProductInventory> inventories = productInventoryRepository.findAllByProductIdInAndGroupId(productIds, groupId);

        // Xóa groupId khỏi các bản ghi inventory
        inventories.forEach(inventory -> inventory.setGroupId(null));
        productInventoryRepository.saveAll(inventories);
    }

    private InventoryDto convertToDto(ProductInventory inventory) {
        if (inventory == null) {
            return null;
        }

        return InventoryDto.builder()
                .inventoryId(inventory.getInventoryId())
                .groupId(inventory.getGroupId())
                .productId(inventory.getProductId())
                .variant(inventory.getVariant())
                .productName(inventory.getProductName())
                .color(inventory.getColor())
                .quantity(inventory.getQuantity())
                .originalPrice(inventory.getOriginalPrice())
                .currentPrice(inventory.getCurrentPrice())
                .build();
    }

    private ProductInventory convertToEntity(InventoryDto dto) {
        return ProductInventory.builder()
                .inventoryId(dto.getInventoryId())
                .groupId(dto.getGroupId())
                .productId(dto.getProductId())
                .variant(dto.getVariant())
                .productName(dto.getProductName())
                .color(dto.getColor())
                .quantity(dto.getQuantity())
                .originalPrice(dto.getOriginalPrice())
                .currentPrice(dto.getCurrentPrice())
                .build();
    }

    @Transactional(readOnly = true)
    public List<GroupWithProductsDto> getAllProductsByGroup(Pageable pageable) {
        try {
            Page<GroupVariants> groupsPage = groupVariantsRepository.findAll(pageable);
            List<GroupVariants> groups = groupsPage.getContent();

            log.debug("Found {} total groups in database, fetching page {} with {} groups",
                    groupsPage.getTotalElements(), pageable.getPageNumber(), groups.size());

            if (groups.isEmpty()) {
                log.info("No groups found for page={}, size={}",
                        pageable.getPageNumber(), pageable.getPageSize());
                return Collections.emptyList();
            }

            log.debug("Fetching groups with priority numbers: {}",
                    groups.stream()
                            .map(g -> String.format("%s (priority: %d)",
                                    g.getGroupId(), g.getPriorityNumber()))
                            .collect(Collectors.joining(", ")));

            List<GroupWithProductsDto> result = new ArrayList<>();

            for (GroupVariants group : groups) {
                GroupWithProductsDto groupWithProducts = processGroupWithProducts(group);
                if (groupWithProducts != null) {
                    result.add(groupWithProducts);
                }
            }

            log.info("Successfully fetched {} groups with their products", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error getting all products by group: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private List<GroupProductDto> processProductsInGroup(List<ProductInventory> inventories) {
        List<InventoryDto> inventoryDtos = inventories.stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .filter(dto -> dto.getProductId() != null)
                .collect(Collectors.toList());

        if (inventoryDtos.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<InventoryDto>> productGroups = new HashMap<>();

        for (InventoryDto dto : inventoryDtos) {
            String variant = dto.getVariant() != null ? dto.getVariant() : "null";
            String productId = dto.getProductId();
            String groupKey = variant + "|" + productId;

            if (!productGroups.containsKey(groupKey)) {
                productGroups.put(groupKey, new ArrayList<>());
            }
            productGroups.get(groupKey).add(dto);
        }

        List<GroupProductDto> groupProducts = new ArrayList<>();

        for (Map.Entry<String, List<InventoryDto>> entry : productGroups.entrySet()) {
            String[] keyParts = entry.getKey().split("\\|");
            String variantKey = keyParts[0];
            List<InventoryDto> productInventories = entry.getValue();

            if (productInventories.isEmpty()) {
                continue;
            }

            // Lấy thông tin sản phẩm từ phần tử đầu tiên
            InventoryDto firstInventory = productInventories.get(0);
            String productName = firstInventory != null && firstInventory.getProductName() != null ?
                    firstInventory.getProductName() : "";

            String displayVariant = "null".equals(variantKey) ? null : variantKey;

            try {
                // Sắp xếp theo màu để đảm bảo thứ tự nhất quán
                productInventories.sort(Comparator.comparing(
                        dto -> dto.getColor() != null ? dto.getColor() : "",
                        Comparator.nullsLast(String::compareTo)
                ));
            } catch (Exception e) {
                log.warn("Error sorting by color: {}", e.getMessage());
            }


            String originalPrice = null;
            String currentPrice = null;

            if (!productInventories.isEmpty()) {
                InventoryDto firstColorVariant = productInventories.get(0);
                originalPrice = firstColorVariant.getOriginalPrice() != null ? firstColorVariant.getOriginalPrice() : null;
                currentPrice = firstColorVariant.getCurrentPrice() != null ? firstColorVariant.getCurrentPrice() : null;
            }

            GroupProductDto groupProductDto = GroupProductDto.builder()
                    .productName(productName)
                    .variant(displayVariant)
                    .originalPrice(originalPrice)
                    .currentPrice(currentPrice)
                    .build();

            groupProducts.add(groupProductDto);
        }

        return groupProducts;
    }

    private GroupWithProductsDto processGroupWithProducts(GroupVariants group) {
        try {
            if (group == null || group.getGroupId() == null) {
                return null;
            }

            log.debug("Processing group with ID: {}, priority: {}",
                    group.getGroupId(), group.getPriorityNumber());

            List<ProductInventory> inventories = productInventoryRepository.findAllByGroupId(group.getGroupId());

            if (inventories == null || inventories.isEmpty()) {
                log.debug("No inventories found for group ID: {}", group.getGroupId());
                return GroupWithProductsDto.builder()
                        .groupVariantsDto(GroupVariantsDto.builder()
                                .groupId(group.getGroupId())
                                .priorityNumber(group.getPriorityNumber())
                                .image(group.getImage())
                                .build())
                        .products(Collections.emptyList())
                        .build();
            }

            List<GroupProductDto> groupProducts = processProductsInGroup(inventories);

            log.debug("Found {} products for group ID: {}", groupProducts.size(), group.getGroupId());

            return GroupWithProductsDto.builder()
                    .groupVariantsDto(GroupVariantsDto.builder()
                            .groupId(group.getGroupId())
                            .priorityNumber(group.getPriorityNumber())
                            .image(group.getImage())
                            .build())
                    .products(groupProducts)
                    .build();

        } catch (Exception e) {
            log.error("Error processing group with ID {}: {}",
                    group != null ? group.getGroupId() : "null", e.getMessage(), e);
            return null;
        }
    }
}