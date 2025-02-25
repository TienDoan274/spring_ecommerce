package com.eazybytes.repository;

import com.eazybytes.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByType(String type);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByBrand(String brand);

}