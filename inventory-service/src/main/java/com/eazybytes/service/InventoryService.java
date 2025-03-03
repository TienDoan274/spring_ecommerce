package com.eazybytes.service;

import com.eazybytes.dto.UpdateQuantityRequest;
import com.eazybytes.exception.InventoryNotFoundException;
import com.eazybytes.model.PhoneInventory;
import com.eazybytes.model.LaptopInventory;
import com.eazybytes.repository.PhoneInventoryRepository;
import com.eazybytes.repository.LaptopInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final PhoneInventoryRepository phoneInventoryRepository;
    private final LaptopInventoryRepository laptopInventoryRepository;

    /**
     * Cập nhật số lượng điện thoại
     */
    @Transactional
    public PhoneInventory updatePhoneQuantity(UpdateQuantityRequest request) {
        PhoneInventory inventory = phoneInventoryRepository
                .findByPhoneIdAndColor(request.getProductId(), request.getColor())
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho điện thoại với ID: " + request.getProductId() +
                                " và màu: " + request.getColor()));

        inventory.setQuantity(request.getQuantity());
        return phoneInventoryRepository.save(inventory);
    }

    /**
     * Cập nhật số lượng laptop
     */
    @Transactional
    public LaptopInventory updateLaptopQuantity(UpdateQuantityRequest request) {
        LaptopInventory inventory = laptopInventoryRepository
                .findByLaptopIdAndColor(request.getProductId(), request.getColor())
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho laptop với ID: " + request.getProductId() +
                                " và màu: " + request.getColor()));

        inventory.setQuantity(request.getQuantity());
        return laptopInventoryRepository.save(inventory);
    }

    /**
     * Giảm số lượng điện thoại
     */
    @Transactional
    public PhoneInventory decreasePhoneQuantity(String phoneId, String color, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng giảm phải lớn hơn 0");
        }

        PhoneInventory inventory = phoneInventoryRepository
                .findByPhoneIdAndColor(phoneId, color)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho điện thoại với ID: " + phoneId +
                                " và màu: " + color));

        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Không đủ số lượng trong kho. Hiện có: " + inventory.getQuantity() +
                            ", Yêu cầu: " + quantity);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        return phoneInventoryRepository.save(inventory);
    }

    /**
     * Giảm số lượng laptop
     */
    @Transactional
    public LaptopInventory decreaseLaptopQuantity(String laptopId, String color, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng giảm phải lớn hơn 0");
        }

        LaptopInventory inventory = laptopInventoryRepository
                .findByLaptopIdAndColor(laptopId, color)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho laptop với ID: " + laptopId +
                                " và màu: " + color));

        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Không đủ số lượng trong kho. Hiện có: " + inventory.getQuantity() +
                            ", Yêu cầu: " + quantity);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        return laptopInventoryRepository.save(inventory);
    }

    /**
     * Tăng số lượng điện thoại
     */
    @Transactional
    public PhoneInventory increasePhoneQuantity(String phoneId, String color, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng tăng phải lớn hơn 0");
        }

        PhoneInventory inventory = phoneInventoryRepository
                .findByPhoneIdAndColor(phoneId, color)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho điện thoại với ID: " + phoneId +
                                " và màu: " + color));

        inventory.setQuantity(inventory.getQuantity() + quantity);
        return phoneInventoryRepository.save(inventory);
    }

    /**
     * Tăng số lượng laptop
     */
    @Transactional
    public LaptopInventory increaseLaptopQuantity(String laptopId, String color, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng tăng phải lớn hơn 0");
        }

        LaptopInventory inventory = laptopInventoryRepository
                .findByLaptopIdAndColor(laptopId, color)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho laptop với ID: " + laptopId +
                                " và màu: " + color));

        inventory.setQuantity(inventory.getQuantity() + quantity);
        return laptopInventoryRepository.save(inventory);
    }
}