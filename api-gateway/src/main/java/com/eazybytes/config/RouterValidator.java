package com.eazybytes.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/products",
            "/api/products/category/{category}",
            "/api/products/search",
            "/api/products/search/{id}"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> matchPath(request.getURI().getPath(), uri));

    private boolean matchPath(String requestPath, String pattern) {
        // Kiểm tra nếu pattern chứa biến đường dẫn
        boolean hasPathVariable = pattern.contains("{") && pattern.contains("}");

        // Nếu là endpoint cố định (không có biến đường dẫn)
        if (!hasPathVariable) {
            return requestPath.equals(pattern);
        }

        // Trường hợp có biến đường dẫn
        String[] requestParts = requestPath.split("/");
        String[] patternParts = pattern.split("/");

        if (requestParts.length != patternParts.length) {
            return false;
        }

        for (int i = 0; i < patternParts.length; i++) {
            // Nếu là biến đường dẫn, chấp nhận bất kỳ giá trị nào
            if (patternParts[i].startsWith("{") && patternParts[i].endsWith("}")) {
                continue;
            }
            // Nếu không phải biến đường dẫn và không khớp
            if (!patternParts[i].equals(requestParts[i])) {
                return false;
            }
        }
        return true;
    }
}