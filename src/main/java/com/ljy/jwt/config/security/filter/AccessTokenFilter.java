package com.ljy.jwt.config.security.filter;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

public class AccessTokenFilter extends AbstractAuthenticationProcessingFilter {
    private String usernameParamName = "id";
    private String passwordParamName = "password";
    private String emptyLoginInfoMessage = "아이디와 비밀번호 모두 입력해주세요.";

    public AccessTokenFilter(String loginUrl, HttpMethod method) {
        super(new AntPathRequestMatcher(loginUrl, method.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        String username = httpServletRequest.getParameter(usernameParamName);
        String password = httpServletRequest.getParameter(passwordParamName);
        verifyExistAll(username, password);
        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(username.trim(), password.trim(), Collections.emptyList()));
    }

    private void verifyExistAll(String username, String password) {
        if(!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new AuthenticationServiceException(emptyLoginInfoMessage);
        }
    }
}
