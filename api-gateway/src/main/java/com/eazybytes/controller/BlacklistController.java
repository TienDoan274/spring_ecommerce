// src/main/java/com/eazybytes/controller/BlacklistController.java
package com.eazybytes.controller;

import com.eazybytes.security.JwtUtils;
import com.eazybytes.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/blacklist")
@RequiredArgsConstructor
public class BlacklistController {
    private final TokenBlacklistService blacklistService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<Void> blacklistToken(@RequestBody TokenBlacklistRequest request) {
        String token = request.getToken();
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Calculate remaining time until token expiration
        long expirationTimeInSeconds = jwtUtils.getTokenExpirationTime(token);
        if (expirationTimeInSeconds > 0) {
            blacklistService.addToBlacklist(token, expirationTimeInSeconds);
        }

        return ResponseEntity.ok().build();
    }

    public static class TokenBlacklistRequest {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}