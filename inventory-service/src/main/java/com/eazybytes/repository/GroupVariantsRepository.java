package com.eazybytes.repository;

import com.eazybytes.model.GroupVariants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupVariantsRepository extends JpaRepository<GroupVariants, Long> {
    Page<GroupVariants> findAll(Pageable pageable);

    @Query("SELECT MAX(g.priorityNumber) FROM GroupVariants g")
    Integer findHighestPriorityNumber();
}