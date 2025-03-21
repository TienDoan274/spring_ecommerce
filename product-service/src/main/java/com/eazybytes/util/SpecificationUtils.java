package com.eazybytes.util;

import com.eazybytes.dto.ProductResponse.Specification;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class SpecificationUtils {

    /**
     * Thêm một thông số vào danh sách nếu giá trị không null
     *
     * @param specs Danh sách thông số
     * @param name  Tên thông số
     * @param value Giá trị thông số
     */
    public void addSpecification(List<Specification> specs, String name, Object value) {
        if (value != null) {
            specs.add(Specification.builder()
                    .name(name)
                    .value(value)
                    .build());
        }
    }

    /**
     * Thêm nhiều thông số từ một Map
     *
     * @param specs Danh sách thông số
     * @param specsMap Map chứa các cặp tên-giá trị thông số
     */
    public void addSpecifications(List<Specification> specs, Map<String, Object> specsMap) {
        specsMap.forEach((name, value) -> {
            if (value != null) {
                addSpecification(specs, name, value);
            }
        });
    }

    /**
     * Thêm các thông số phân nhóm
     *
     * @param specs Danh sách thông số
     * @param groupTitle Tiêu đề nhóm (có thể null nếu không cần phân nhóm)
     * @param specsMap Map chứa các cặp tên-giá trị thông số
     */
    public void addSpecificationGroup(List<Specification> specs, String groupTitle, Map<String, Object> specsMap) {
        // Nếu có tiêu đề nhóm, thêm một spec đánh dấu nhóm
        if (groupTitle != null && !specsMap.isEmpty()) {
            addSpecification(specs, "group:" + groupTitle, null);
        }

        // Thêm các thông số trong nhóm
        addSpecifications(specs, specsMap);
    }

    /**
     * Tạo một nhóm thông số từ đối tượng
     *
     * @param object Đối tượng chứa các trường thông số
     * @param fieldNames Danh sách tên trường
     * @param displayNames Danh sách tên hiển thị tương ứng
     * @return Map chứa các cặp tên hiển thị - giá trị
     */
    public Map<String, Object> createSpecsFromObject(Object object, List<String> fieldNames, List<String> displayNames) {
        // Implementation sẽ phụ thuộc vào Java Reflection API
        // Code này phức tạp hơn, có thể triển khai sau
        return Map.of(); // Placeholder
    }
}