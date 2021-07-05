package com.ljy.jwt.security;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtAuthenticationFilter implements Filter {
	private final JwtTokenResolver jwtTokenResolver;
	private final JwtAuthenticationToken jwtAuthenticationToken;
	private final TokenStore tokenStore;
	
	@Value("${spring.jwt.secretKey}")
	private String secretKey;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		JwtToken token = getJwtToken(request);
		if (isEmptyToken(token)) {
			chain.doFilter(request, response);
			return;
		}

		token.validation(secretKey);

		String userIdentifier = getUserIdentifier(token);
		JwtToken findToken = getPersistedToken(userIdentifier).orElse(null);
		
		if (isEmptyToken(token) || notEqualAccessToken(token, findToken)) {
			chain.doFilter(request, response);
			return;
		}
		
		setAuthentication(token);
		chain.doFilter(request, response);
	}

	private boolean notEqualAccessToken(JwtToken token, JwtToken findToken) {
		return !findToken.equals(token);
	}

	private Optional<JwtToken> getPersistedToken(String userIdentifier) {
		return tokenStore.findByUserIdentifier(userIdentifier);
	}

	private String getUserIdentifier(JwtToken token) {
		return jwtAuthenticationToken.getUserIdentifierFromToken(secretKey, token);
	}

	private JwtToken getJwtToken(ServletRequest request) {
		return jwtTokenResolver.resolve((HttpServletRequest) request);
	}

	private boolean isEmptyToken(JwtToken token) {
		return Objects.isNull(token);
	}

	private void setAuthentication(JwtToken jwtToken) {
		Authentication authentication = jwtAuthenticationToken.getAuthentication(secretKey, jwtToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public JwtAuthenticationFilter(JwtTokenResolver jwtTokenResolver, JwtAuthenticationToken jwtAuthenticationToken, TokenStore tokenStore) {
		this.jwtTokenResolver = jwtTokenResolver;
		this.jwtAuthenticationToken = jwtAuthenticationToken;
		this.tokenStore = tokenStore;
	}

}
