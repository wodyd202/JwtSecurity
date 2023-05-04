package com.ljy.jwt.application.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class GetUserResource {
    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("username")
    private final String username;
    @JsonProperty("created_at")
    private final LocalDateTime createdAt;

    public GetUserResource(String userId, String username, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }
}
