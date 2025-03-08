package com.eazybytes.repository;

import com.eazybytes.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneRepository extends MongoRepository<Phone, String> {
    List<Phone> findByType(String type);
    List<Phone> findByNameContainingIgnoreCase(String name);
    List<Phone> findByBrand(String brand);
    boolean existsByName(String name);

}