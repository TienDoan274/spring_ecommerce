// Repository
package com.eazybytes.repository;

import com.eazybytes.model.PhoneInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneInventoryRepository extends JpaRepository<PhoneInventory, Long> {
    Optional<PhoneInventory> findByPhoneIdAndColor(String phoneId, String color);
}