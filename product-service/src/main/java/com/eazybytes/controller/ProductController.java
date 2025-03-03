package com.eazybytes.controller;

import com.eazybytes.dto.*;
import com.eazybytes.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

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
        return productService.createPhone(phoneRequest);
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
        productService.deletePhone(id);
    }

    @DeleteMapping("/deleteLaptop/{id}")
    @PreAuthorize("@roleChecker.hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLaptop(@PathVariable String id) {
        productService.deleteLaptop(id);
    }
}
