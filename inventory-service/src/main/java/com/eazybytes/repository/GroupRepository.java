package com.eazybytes.repository;

import com.eazybytes.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    List<Group> findAllByType(String type);

    int countByType(String type);

    @Query("SELECT MAX(g.orderNumber) FROM Group g WHERE g.type = :type")
    Integer findMaxOrderNumberByType(@Param("type") String type);

    @Query("SELECT g FROM Group g WHERE g.type = :type AND " +
            "(SELECT COUNT(DISTINCT gt.tag.id) FROM GroupTags gt WHERE gt.group = g AND gt.tag.tagName IN :tags) = :tagCount")
    List<Group> findByTypeAndAllTags(@Param("type") String type, @Param("tags") List<String> tags, @Param("tagCount") long tagCount);

    @Query("SELECT COUNT(DISTINCT g) FROM Group g WHERE g.type = :type AND " +
            "(SELECT COUNT(DISTINCT gt.tag.id) FROM GroupTags gt WHERE gt.group = g AND gt.tag.tagName IN :tags) = :tagCount")
    long countByTypeAndAllTags(@Param("type") String type, @Param("tags") List<String> tags, @Param("tagCount") long tagCount);

    // Tìm groups chứa tất cả tags (không quan tâm type)
    @Query("SELECT g FROM Group g WHERE " +
            "(SELECT COUNT(DISTINCT gt.tag.id) FROM GroupTags gt WHERE gt.group = g AND gt.tag.tagName IN :tags) = :tagCount")
    List<Group> findByAllTags(@Param("tags") List<String> tags, @Param("tagCount") long tagCount);

    @Query("SELECT DISTINCT g FROM Group g JOIN g.groupTags gt JOIN gt.tag t " +
            "WHERE g.type = :type AND t.tagName IN :tags")
    List<Group> findAllByTypeAndTagNames(@Param("type") String type, @Param("tags") List<String> tags);
}