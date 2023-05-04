package com.ljy.jwt.config.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class JwtToken {
    @JsonProperty("access_token")
    private final String accessToken;
    @JsonProperty("refresh_token")
    private final String refreshToken;
    @JsonProperty("created_at")
    private final LocalDateTime createdAt;

    public JwtToken(String accessToken,
                    String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.createdAt = LocalDateTime.now();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
