package com.eazybytes.controller;

import com.eazybytes.client.InventoryClient;
import com.eazybytes.dto.*;
import com.eazybytes.service.ProductService;
import jakarta.validation.Valid;
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

//    @GetMapping("/type/{type}")
//    @ResponseStatus(HttpStatus.OK)
//    public List<?> getProductsByType(@PathVariable String type) {
//        if ("laptop".equals(type))
//            return productService.ge();
//        else if("phone".equals(type))
//            return productService.getAllPhones();
//        else{
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Invalid product type: " + type + ". Supported types are 'laptop' and 'phone'."
//            );
//        }
//    }

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


//    @GetMapping("/searchPhones")
//    @ResponseStatus(HttpStatus.OK)
//    public List<PhoneResponse> searchPhones(@RequestParam String name) {
//        return productService.getPhonesByNames(name);
//    }
//
//    @GetMapping("/searchLaptops")
//    @ResponseStatus(HttpStatus.OK)
//    public List<LaptopResponse> searchLaptops(@RequestParam String name) {
//        return productService.getLaptopsByNames(name);
//    }

    @PostMapping("/createPhone")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneResponse createPhone(@RequestBody @Valid PhoneWithInventoryRequest phoneWithInventoryRequest) {
        // Tạo phone trước
        PhoneResponse phoneResponse = productService.createPhone(phoneWithInventoryRequest);
        String phoneId = phoneResponse.getProductId();

        return phoneResponse;
    }

    @PostMapping("/createLaptop")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public LaptopResponse createLaptop(@RequestBody @Valid LaptopWithInventoryRequest laptopWithInventoryRequest) {
        LaptopResponse laptopResponse = productService.createLaptop(laptopWithInventoryRequest);
        String laptopId = laptopResponse.getProductId();

        return laptopResponse;
    }


    @PutMapping("/updatePhone/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public PhoneResponse updatePhone(@PathVariable String id, @RequestBody PhoneRequest phoneRequest,@RequestBody List<InventoryDto> inventoryDtos) {
        return productService.updatePhone(id, phoneRequest,inventoryDtos);
    }

    @PutMapping("/updateLaptop/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public LaptopResponse updateLaptop(@PathVariable String id, @RequestBody LaptopRequest laptopRequest,@RequestBody List<InventoryDto> inventoryDtos) {
        return productService.updateLaptop(id, laptopRequest,inventoryDtos);
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
