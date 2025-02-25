package com.eazybytes.controller;

import com.eazybytes.dto.*;
import com.eazybytes.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @GetMapping("/category/{category}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProducts(@RequestParam String name) {
        return productService.searchProducts(name);
    }

    @PostMapping("/phone")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneResponse createPhone(@RequestBody PhoneRequest phoneRequest) {
        return productService.createPhone(phoneRequest);
    }

    @PostMapping("/laptop")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public LaptopResponse createLaptop(@RequestBody LaptopRequest laptopRequest) {
        return productService.createLaptop(laptopRequest);
    }


    @PutMapping("/phone/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public PhoneResponse updatePhone(@PathVariable String id, @RequestBody PhoneRequest phoneRequest) {
        return productService.updatePhone(id, phoneRequest);
    }

    @PutMapping("/laptop/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public LaptopResponse updateLaptop(@PathVariable String id, @RequestBody LaptopRequest laptopRequest) {
        return productService.updateLaptop(id, laptopRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}
