// src/main/java/com/eazybytes/model/Product.java
package com.eazybytes.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

//@Document(collection = "Product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String brand;
    private List<Map<String,String>> images;
    private Boolean isAvailable;
    private String type;  // "PHONE" or "LAPTOP"
    private String warrantyPeriod; // thời gian bảo hành
    private List<Map<String,String>> productReviews; // bài đánh giá sản phẩm
    private List<String> promotions; // chương trình khuyến mãi
    private String release; // thời điểm ra mắt
}