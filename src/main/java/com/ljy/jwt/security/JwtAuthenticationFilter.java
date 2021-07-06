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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ljy.jwt.exception.InvalidJwtTokenException;
import com.ljy.jwt.security.JwtToken.JwtTokenType;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthenticationFilter implements Filter {
	private final JwtTokenResolver jwtTokenResolver;
	private final JwtAuthenticationToken jwtAuthenticationToken;
	private final JwtTokenStore tokenStore;
	private final String secretKey;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		JwtToken token = getJwtToken(request);
		if (isEmptyToken(token)) {
			chain.doFilter(request, response);
			return;
		}
		
		try {
			token.validation(secretKey, JwtTokenType.ACCESS_TOKEN);
		}catch (ExpiredJwtException e) {
			removePersistAccessToken(token);
			chain.doFilter(request, response);
			return;
		}catch (InvalidJwtTokenException e) {
			chain.doFilter(request, response);
			return;
		}

		String userIdentifier = getUserIdentifier(token);
		Object findToken = getPersistedToken(userIdentifier).orElse(null);
		
		if (isEmptyToken(findToken) || notEqualAccessToken(token, findToken)) {
			chain.doFilter(request, response);
			return;
		}

		setAuthentication(token);
		chain.doFilter(request, response);
	}

	private void removePersistAccessToken(JwtToken token) {
		String userIdentifier = getUserIdentifier(token);
		tokenStore.remove(userIdentifier);
	}

	private boolean notEqualAccessToken(Object token, Object findToken) {
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

	private boolean isEmptyToken(Object token) {
		return Objects.isNull(token);
	}

	private void setAuthentication(JwtToken jwtToken) {
		Authentication authentication = jwtAuthenticationToken.getAuthentication(secretKey, jwtToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public JwtAuthenticationFilter(JwtTokenResolver jwtTokenResolver, JwtAuthenticationToken jwtAuthenticationToken, JwtTokenStore tokenStore, String secretKey) {
		this.jwtTokenResolver = jwtTokenResolver;
		this.jwtAuthenticationToken = jwtAuthenticationToken;
		this.tokenStore = tokenStore;
		this.secretKey = secretKey;
	}

}
