package com.ljy.jwt.config.security.filter;

import com.ljy.jwt.config.security.jwt.JwtUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RefreshTokenFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtUtils jwtUtils;

    public RefreshTokenFilter(String refreshTokenUrl, HttpMethod method, JwtUtils jwtUtils) {
        super(new AntPathRequestMatcher(refreshTokenUrl, method.name()));
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        String token = JwtTokenGetter.get(httpServletRequest, "Authorization");
        if (jwtUtils.validate(token) && jwtUtils.isRefreshToken(token)) {
            Authentication authentication = jwtUtils.getAuthentication(token);
            return new UsernamePasswordAuthenticationToken(authentication, authentication.getCredentials());
        }
        throw new BadCredentialsException("토큰 정보가 올바르지 않습니다.");
    }
}
