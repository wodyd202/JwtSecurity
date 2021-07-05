package com.ljy.jwt.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

public class SimpleJwtTokenResolver implements JwtTokenResolver {

	@Value("${spring.jwt.header}")
	private String X_AUTH_TOKEN_HEADER;

	public JwtToken resolve(HttpServletRequest request) {
		String token = request.getHeader(X_AUTH_TOKEN_HEADER);
		if (isEmptyHeader(token)) {
			return null;
		}
		return new JwtToken(token);
	}

	private boolean isEmptyHeader(String reqJwtToken) {
		return !StringUtils.hasText(reqJwtToken);
	}
}
