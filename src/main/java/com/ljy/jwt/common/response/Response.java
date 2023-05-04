package com.ljy.jwt.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

public class Response<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;
    private final int code;

    public Response() {
        this.data = null;
        this.message = null;
        this.code = 0;
    }

    public Response(T data, String message, int code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(data, null, HttpStatus.OK.value());
    }

    public static Response<?> badRequest(String message) {
        return new Response<>(null, message, HttpStatus.BAD_REQUEST.value());
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
