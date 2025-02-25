package com.eazybytes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
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
    private Boolean isAvailable;

}