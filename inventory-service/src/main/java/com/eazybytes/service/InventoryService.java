package com.eazybytes.service;

import com.eazybytes.dto.InventoryDto;
import com.eazybytes.exception.InventoryAlreadyExistsException;
import com.eazybytes.exception.InventoryNotFoundException;
import com.eazybytes.model.ProductInventory;
import com.eazybytes.repository.ProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductInventoryRepository productInventoryRepository;

    public ProductInventory getProductInventory(String productId, String color) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống");
        }

        Optional<ProductInventory> inventoryOptional = productInventoryRepository
                .findByProductIdAndColor(productId, color);

        if (!inventoryOptional.isPresent()) {
            throw new InventoryNotFoundException(
                    "Không tìm thấy tồn kho cho sản phẩm có ID: " + productId +
                            " và màu: " + color);
        }

        return inventoryOptional.get();
    }


    @Transactional
    public ProductInventory createProductInventory(InventoryDto request) {
        Optional<ProductInventory> existingInventory = productInventoryRepository
                .findByProductIdAndColor(request.getProductId(), request.getColor());

        ProductInventory inventory;
        if (existingInventory.isPresent()) {
            throw new InventoryAlreadyExistsException(
                    "Tồn kho đã tồn tại cho sản phẩm với ID: " + request.getProductId() +
                            " và màu: " + request.getColor());
        } else {
            inventory = new ProductInventory();
            inventory.setProductId(request.getProductId());
            inventory.setColor(request.getColor());

        }
        inventory.setVariant(request.getVariant());
        inventory.setProductName(request.getProductName());
        inventory.setQuantity(request.getQuantity());
        inventory.setOriginalPrice(request.getOriginalPrice());
        inventory.setCurrentPrice(request.getCurrentPrice());

        return productInventoryRepository.save(inventory);
    }


    @Transactional
    public ProductInventory updateProductInventory(InventoryDto request) {
        Optional<ProductInventory> existingInventory = productInventoryRepository
                .findByProductIdAndColor(request.getProductId(), request.getColor());

        ProductInventory inventory;

        if (existingInventory.isPresent()) {
            inventory = existingInventory.get();
        } else {
            inventory = new ProductInventory();
            inventory.setProductId(request.getProductId());
            inventory.setColor(request.getColor());
        }
        inventory.setVariant(request.getVariant());
        inventory.setProductName(request.getProductName());
        inventory.setQuantity(request.getQuantity());
        inventory.setOriginalPrice(request.getOriginalPrice());
        inventory.setCurrentPrice(request.getCurrentPrice());

        return productInventoryRepository.save(inventory);
    }

    
    @Transactional
    public ProductInventory decreaseProductQuantity(String phoneId, String color, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng giảm phải lớn hơn 0");
        }

        ProductInventory inventory = productInventoryRepository
                .findByProductIdAndColor(phoneId, color)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho điện thoại với ID: " + phoneId +
                                " và màu: " + color));

        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Không đủ số lượng trong kho. Hiện có: " + inventory.getQuantity() +
                            ", Yêu cầu: " + quantity);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        return productInventoryRepository.save(inventory);
    }

    /**
     * Tăng số lượng điện thoại
     */
    @Transactional
    public ProductInventory increaseProductQuantity(String phoneId, String color, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng tăng phải lớn hơn 0");
        }

        ProductInventory inventory = productInventoryRepository
                .findByProductIdAndColor(phoneId, color)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho điện thoại với ID: " + phoneId +
                                " và màu: " + color));

        inventory.setQuantity(inventory.getQuantity() + quantity);
        return productInventoryRepository.save(inventory);
    }

    public List<InventoryDto> findAllColorVariantsByProductId(String productId) {
        List<ProductInventory> inventories = productInventoryRepository.findAllByProductId(productId);

        // Chuyển đổi từ entity thành DTO
        return inventories.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private InventoryDto mapToDto(ProductInventory inventory) {
        return InventoryDto.builder()
                .inventoryId(inventory.getInventoryId())
                .productId(inventory.getProductId())
                .variant(inventory.getVariant())
                .productName(inventory.getProductName())
                .color(inventory.getColor())
                .quantity(inventory.getQuantity())
                .originalPrice(inventory.getOriginalPrice())
                .currentPrice(inventory.getCurrentPrice())
                .build();
    }

    @Transactional
    public void deleteAllByProductId(String productId) {

        productInventoryRepository.deleteAllByProductId(productId);
    }

    @Transactional
    public void deleteProductInventory(String productId, String color){
        productInventoryRepository.deleteByProductIdAndColor(productId,color);
    }
}