package com.eazybytes.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal originalPrice; // giá gốc
    private BigDecimal currentPrice; // giá khuyến mãi
    private String brand;
    private String image;
    private Integer stockQuantity;
    private String type;  // "PHONE" or "LAPTOP"
    private String warrantyPeriod; // thời gian bảo hành
    private Map<String,String> productReview; // bài đánh giá sản phẩm
    private List<String> promotion; // chương trình khuyến mãi
    private String release; // thời điểm ra mắt
}