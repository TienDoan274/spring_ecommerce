

package com.eazybytes.repository;

import com.eazybytes.model.ProductInventory;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {




    Optional<ProductInventory> findByProductIdAndColor(String productId, String color);

    @Modifying
    @Transactional
    void deleteAllByProductId(String productId);
}
