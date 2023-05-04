package com.ljy.jwt.config.security.filter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.jwt.common.response.Response;
import com.ljy.jwt.config.security.jwt.JwtToken;
import com.ljy.jwt.config.security.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    public TokenSuccessHandler(JwtUtils jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtUtils = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        JwtToken jwtToken = jwtUtils.generateToken(authentication);
        String response = objectMapper.writeValueAsString(Response.ok(jwtToken));
        httpServletResponse.getWriter().write(response);
        httpServletResponse.setStatus(HttpStatus.OK.value());
    }
}
