package com.eazybytes.service;

import com.eazybytes.dto.CreateInventoryRequest;
import com.eazybytes.dto.UpdateInventoryRequest;
import com.eazybytes.exception.InventoryAlreadyExistsException;
import com.eazybytes.exception.InventoryNotFoundException;
import com.eazybytes.model.PhoneInventory;
import com.eazybytes.model.LaptopInventory;
import com.eazybytes.repository.PhoneInventoryRepository;
import com.eazybytes.repository.LaptopInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final PhoneInventoryRepository phoneInventoryRepository;
    private final LaptopInventoryRepository laptopInventoryRepository;

    @Transactional
    public PhoneInventory createPhoneInventory(CreateInventoryRequest request) {
        // Kiểm tra xem tồn kho đã tồn tại chưa
        Optional<PhoneInventory> existingInventory = phoneInventoryRepository
                .findByPhoneIdAndColor(request.getProductId(), request.getColor());

        PhoneInventory inventory;
        if (existingInventory.isPresent()) {
            // Nếu đã tồn tại, có thể ném ngoại lệ hoặc cập nhật
            throw new InventoryAlreadyExistsException(
                    "Tồn kho đã tồn tại cho phone với ID: " + request.getProductId() +
                            " và màu: " + request.getColor());
        } else {
            inventory = new PhoneInventory();
            inventory.setPhoneId(request.getProductId());
            inventory.setColor(request.getColor());
        }

        inventory.setQuantity(request.getQuantity());
        inventory.setOriginalPrice(request.getOriginalPrice());
        inventory.setCurrentPrice(request.getCurrentPrice());
        return phoneInventoryRepository.save(inventory);
    }

    @Transactional
    public LaptopInventory createLaptopInventory(CreateInventoryRequest request) {
        Optional<LaptopInventory> existingInventory = laptopInventoryRepository
                .findByLaptopIdAndColor(request.getProductId(), request.getColor());

        LaptopInventory inventory;
        if (existingInventory.isPresent()) {
            throw new InventoryAlreadyExistsException(
                    "Tồn kho đã tồn tại cho laptop với ID: " + request.getProductId() +
                            " và màu: " + request.getColor());
        } else {
            inventory = new LaptopInventory();
            inventory.setLaptopId(request.getProductId());
            inventory.setColor(request.getColor());
        }

        inventory.setQuantity(request.getQuantity());
        inventory.setOriginalPrice(request.getOriginalPrice());
        inventory.setCurrentPrice(request.getCurrentPrice());
        return laptopInventoryRepository.save(inventory);
    }

    @Transactional
    public PhoneInventory updatePhoneInventory (UpdateInventoryRequest request) {
        PhoneInventory inventory = phoneInventoryRepository
                .findByPhoneIdAndColor(request.getProductId(), request.getColor())
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho điện thoại với ID: " + request.getProductId() +
                                " và màu: " + request.getColor()));

        inventory.setQuantity(request.getQuantity());
        inventory.setOriginalPrice(request.getOriginalPrice());
        inventory.setCurrentPrice(request.getCurrentPrice());

        return phoneInventoryRepository.save(inventory);
    }

    /**
     * Cập nhật số lượng laptop
     */
    @Transactional
    public LaptopInventory updateLaptopInventory(UpdateInventoryRequest request) {
        LaptopInventory inventory = laptopInventoryRepository
                .findByLaptopIdAndColor(request.getProductId(), request.getColor())
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Không tìm thấy tồn kho cho laptop với ID: " + request.getProductId() +
                                " và màu: " + request.getColor()));

        inventory.setQuantity(request.getQuantity());
        inventory.setOriginalPrice(request.getOriginalPrice());
        inventory.setCurrentPrice(request.getCurrentPrice());
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