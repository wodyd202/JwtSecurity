package com.ljy.jwt.application.dto;

public class SignUpUserDto {
    private final String id;
    private final String password;
    private final String username;

    public SignUpUserDto(String id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
