package com.eazybytes.repository;

import com.eazybytes.model.GroupVariants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupVariantsRepository extends JpaRepository<GroupVariants, Long> {

    @Query("SELECT MAX(g.priorityNumber) FROM GroupVariants g")
    Optional<Integer> findMaxPriorityNumber();

}