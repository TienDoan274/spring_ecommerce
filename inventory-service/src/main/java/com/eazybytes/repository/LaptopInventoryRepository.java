

package com.eazybytes.repository;

import com.eazybytes.model.LaptopInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaptopInventoryRepository extends JpaRepository<LaptopInventory, Long> {
    Optional<LaptopInventory> findByLaptopIdAndColor(String laptopId, String color);
}
