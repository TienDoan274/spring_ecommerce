package com.eazybytes.controller;

import com.eazybytes.client.InventoryClient;
import com.eazybytes.dto.*;
import com.eazybytes.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final InventoryClient inventoryClient;

    @GetMapping("/type/{type}")
    @ResponseStatus(HttpStatus.OK)
    public List<?> getProductsByType(@PathVariable String type) {
        if ("laptop".equals(type))
            return productService.getAllLaptops();
        else if("phone".equals(type))
            return productService.getAllPhones();
        else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid product type: " + type + ". Supported types are 'laptop' and 'phone'."
            );
        }
    }

    @GetMapping("/getPhone/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PhoneResponse getPhone(@PathVariable String id) {
        return productService.getPhoneById(id);
    }

    @GetMapping("/getLaptop/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LaptopResponse getLaptop(@PathVariable String id) {
        return productService.getLaptopById(id);
    }

    @GetMapping("/getAllPhone")
    @ResponseStatus(HttpStatus.OK)
    public List<PhoneResponse> getAllPhone() {
        return productService.getAllPhones();
    }

    @GetMapping("/getAllLaptop")
    @ResponseStatus(HttpStatus.OK)
    public List<LaptopResponse> getAllLaptop() {
        return productService.getAllLaptops();
    }


    @GetMapping("/searchPhones")
    @ResponseStatus(HttpStatus.OK)
    public List<PhoneResponse> searchPhones(@RequestParam String name) {
        return productService.getPhonesByNames(name);
    }

    @GetMapping("/searchLaptops")
    @ResponseStatus(HttpStatus.OK)
    public List<LaptopResponse> searchLaptops(@RequestParam String name) {
        return productService.getLaptopsByNames(name);
    }

    @PostMapping("/createPhone")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneResponse createPhone(@RequestBody PhoneRequest phoneRequest) {
        // Tạo phone trước
        PhoneResponse phoneResponse = productService.createPhone(phoneRequest);
        String phoneId = phoneResponse.getId();

        try {
            // Xử lý các variants
            if (phoneRequest.getColors() != null && !phoneRequest.getColors().isEmpty()) {
                // Có các phiên bản màu khác nhau
                for (String color : phoneRequest.getColors()) {
                    CreateInventoryRequest inventoryRequest = CreateInventoryRequest.builder()
                            .productId(phoneId)
                            .quantity(0)             // Khởi tạo với số lượng 0
                            .originalPrice(0)        // Khởi tạo với giá gốc 0
                            .currentPrice(0)         // Khởi tạo với giá hiện tại 0
                            .color(color)
                            .build();

                    // Gọi API inventory service
                    inventoryClient.createPhoneQuantity(inventoryRequest);
                }
            } else {
                // Không có phiên bản màu, tạo một inventory với color trống
                CreateInventoryRequest inventoryRequest = CreateInventoryRequest.builder()
                        .productId(phoneId)
                        .quantity(0)                 // Khởi tạo với số lượng 0
                        .originalPrice(0)            // Khởi tạo với giá gốc 0
                        .currentPrice(0)             // Khởi tạo với giá hiện tại 0
                        .color("")                   // Color trống
                        .build();

                // Gọi API inventory service
                inventoryClient.createPhoneQuantity(inventoryRequest);
            }

        } catch (Exception e) {
            log.error("Error creating inventory for phone ID {}: {}", phoneId, e.getMessage());
            // Tùy vào yêu cầu, bạn có thể tiếp tục hoặc throw exception
        }

        return phoneResponse;
    }

    @PostMapping("/createLaptop")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public LaptopResponse createLaptop(@RequestBody LaptopRequest laptopRequest) {
        return productService.createLaptop(laptopRequest);
    }


    @PutMapping("/updatePhone/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public PhoneResponse updatePhone(@PathVariable String id, @RequestBody PhoneRequest phoneRequest) {
        return productService.updatePhone(id, phoneRequest);
    }

    @PutMapping("/updateLaptop/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public LaptopResponse updateLaptop(@PathVariable String id, @RequestBody LaptopRequest laptopRequest) {
        return productService.updateLaptop(id, laptopRequest);
    }

    @DeleteMapping("/deletePhone/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhone(@PathVariable String id) {
        try {
            // Xóa inventory trước
            inventoryClient.deleteInventoriesByProductId(id);
            log.info("Successfully deleted inventories for phone ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting inventories for phone ID {}: {}", id, e.getMessage());
            // Tùy thuộc vào yêu cầu, bạn có thể quyết định tiếp tục hoặc throw exception
            // Nếu xóa inventory thất bại nhưng vẫn muốn xóa phone, tiếp tục thực hiện
            // Nếu muốn đảm bảo tính toàn vẹn dữ liệu, có thể throw exception ở đây
        }

        // Xóa phone
        productService.deletePhone(id);
    }

    @DeleteMapping("/deleteLaptop/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLaptop(@PathVariable String id) {
        productService.deleteLaptop(id);
    }
}
