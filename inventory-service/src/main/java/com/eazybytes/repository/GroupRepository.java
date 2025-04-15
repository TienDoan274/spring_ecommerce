package com.eazybytes.repository;

import com.eazybytes.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    // Find all groups by type
    List<Group> findAllByType(String type);

    // Count groups by type
    long countByType(String type);

    // Find groups by type and all specified tags (AND logic)
    @Query("SELECT DISTINCT g FROM Group g JOIN g.groupTags gt JOIN gt.tag t WHERE g.type = :type AND t.tagName IN :tagNames GROUP BY g HAVING COUNT(DISTINCT t) = :tagCount")
    List<Group> findByTypeAndAllTags(String type, List<String> tagNames, Long tagCount);

    // Count groups by type and all specified tags
    @Query("SELECT COUNT(DISTINCT g) FROM Group g JOIN g.groupTags gt JOIN gt.tag t WHERE g.type = :type AND t.tagName IN :tagNames GROUP BY g HAVING COUNT(DISTINCT t) = :tagCount")
    long countByTypeAndAllTags(String type, List<String> tagNames, Long tagCount);

    // Find groups by all specified tags (no type filter)
    @Query("SELECT DISTINCT g FROM Group g JOIN g.groupTags gt JOIN gt.tag t WHERE t.tagName IN :tagNames GROUP BY g HAVING COUNT(DISTINCT t) = :tagCount")
    List<Group> findByAllTags(List<String> tagNames, Long tagCount);

    // Count groups by all specified tags (no type filter)
    @Query("SELECT COUNT(DISTINCT g) FROM Group g JOIN g.groupTags gt JOIN gt.tag t WHERE t.tagName IN :tagNames GROUP BY g HAVING COUNT(DISTINCT t) = :tagCount")
    long countByAllTags(List<String> tagNames, Long tagCount);

    @Query("SELECT MAX(g.orderNumber) FROM Group g WHERE g.type = :type")
    Integer findMaxOrderNumberByType(@Param("type") String type);
}