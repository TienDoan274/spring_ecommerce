package com.eazybytes.service;

import com.eazybytes.dto.GroupVariantsDto;
import com.eazybytes.model.GroupVariants;
import com.eazybytes.repository.GroupVariantsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupVariantsService {

    private final GroupVariantsRepository groupVariantsRepository;

    /**
     * Tạo nhóm sản phẩm mới
     *
     * @param dto Thông tin nhóm cần tạo
     * @return GroupVariantsDto Thông tin nhóm đã tạo
     */
    @Transactional
    public GroupVariantsDto createGroupVariants(GroupVariantsDto dto) {
        // Tạo entity từ DTO
        GroupVariants groupVariants = new GroupVariants();
        groupVariants.setInventoryIds(dto.getInventoryIds());

        // Nếu không có priorityNumber, tự động tạo
        if (dto.getPriorityNumber() == null) {
            Integer maxPriority = groupVariantsRepository.findMaxPriorityNumber().orElse(0);
            groupVariants.setPriorityNumber(maxPriority + 1);
        } else {
            groupVariants.setPriorityNumber(dto.getPriorityNumber());
        }

        // Lưu vào database
        GroupVariants savedEntity = groupVariantsRepository.save(groupVariants);
        log.info("Created GroupVariants with ID: {}", savedEntity.getGroupId());

        // Chuyển đổi entity thành DTO để trả về
        return mapToDto(savedEntity);
    }

    /**
     * Lấy thông tin nhóm theo ID
     *
     * @param groupId ID của nhóm cần lấy
     * @return GroupVariantsDto Thông tin nhóm
     * @throws EntityNotFoundException Nếu không tìm thấy nhóm
     */
    public GroupVariantsDto getGroupVariantsById(Long groupId) {
        GroupVariants entity = groupVariantsRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("GroupVariants not found with ID: " + groupId));

        return mapToDto(entity);
    }


    public Page<GroupVariantsDto> getAllGroupVariants(Pageable pageable) {
        // Lấy dữ liệu có phân trang từ repository
        Page<GroupVariants> entitiesPage = groupVariantsRepository.findAll(pageable);

        // Chuyển đổi Page<GroupVariants> sang Page<GroupVariantsDto>
        return entitiesPage.map(this::mapToDto);
    }

    /**
     * Cập nhật thông tin nhóm
     *
     * @param groupId ID của nhóm cần cập nhật
     * @param dto Thông tin mới
     * @return GroupVariantsDto Thông tin nhóm sau khi cập nhật
     * @throws EntityNotFoundException Nếu không tìm thấy nhóm
     */
    @Transactional
    public GroupVariantsDto updateGroupVariants(Long groupId, GroupVariantsDto dto) {
        GroupVariants entity = groupVariantsRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("GroupVariants not found with ID: " + groupId));

        // Cập nhật thông tin
        if (dto.getInventoryIds() != null) {
            entity.setInventoryIds(dto.getInventoryIds());
        }

        if (dto.getPriorityNumber() != null) {
            entity.setPriorityNumber(dto.getPriorityNumber());
        }

        // Lưu vào database
        GroupVariants updatedEntity = groupVariantsRepository.save(entity);
        log.info("Updated GroupVariants with ID: {}", updatedEntity.getGroupId());

        return mapToDto(updatedEntity);
    }

    /**
     * Xóa nhóm theo ID
     *
     * @param groupId ID của nhóm cần xóa
     * @throws EntityNotFoundException Nếu không tìm thấy nhóm
     */
    @Transactional
    public void deleteGroupVariants(Long groupId) {
        if (!groupVariantsRepository.existsById(groupId)) {
            throw new EntityNotFoundException("GroupVariants not found with ID: " + groupId);
        }

        groupVariantsRepository.deleteById(groupId);
        log.info("Deleted GroupVariants with ID: {}", groupId);
    }

    /**
     * Chuyển đổi từ entity sang DTO
     */
    private GroupVariantsDto mapToDto(GroupVariants entity) {
        return GroupVariantsDto.builder()
                .groupId(entity.getGroupId())
                .inventoryIds(entity.getInventoryIds())
                .priorityNumber(entity.getPriorityNumber())
                .build();
    }
}