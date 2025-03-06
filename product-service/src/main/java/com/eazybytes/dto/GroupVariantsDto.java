package com.eazybytes.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupVariantsDto {
    private Long groupId;
    private List<String> productIds;
    private Integer priorityNumber;
}