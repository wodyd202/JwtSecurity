package com.ljy.jwt.config.security.filter;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

class JwtTokenGetter {
    static String get(HttpServletRequest request, String headerName) {
        String authorization = request.getHeader(headerName);
        return StringUtils.hasText(authorization) && authorization.startsWith("Bearer")
                ? authorization.substring(7)
                : null;
    }
}
