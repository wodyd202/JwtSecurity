package com.ljy.jwt.application.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class SignUpUserResource {
    @JsonProperty("user_id")
    private final String userId;
    @JsonProperty("username")
    private final String username;
    @JsonProperty("created_at")
    private final LocalDateTime createdAt;

    public SignUpUserResource() {
        this.userId = null;
        this.username = null;
        this.createdAt = null;
    }

    public SignUpUserResource(String userId, String username, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
