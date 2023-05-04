package com.ljy.jwt.config.security.filter;

import com.ljy.jwt.config.security.jwt.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtTokenProvider) {
        this.jwtUtils = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = JwtTokenGetter.get((HttpServletRequest) servletRequest, "Authorization");
        if(jwtToken != null && jwtUtils.validate(jwtToken) && jwtUtils.isAccessToken(jwtToken)) {
            Authentication authentication = jwtUtils.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
