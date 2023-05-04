package com.ljy.jwt.config.security.filter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.jwt.common.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    public TokenFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        Response<?> response = Response.badRequest(e.getMessage());
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    }
}
