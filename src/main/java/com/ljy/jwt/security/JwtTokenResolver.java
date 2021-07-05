package com.ljy.jwt.security;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenResolver {
	JwtToken resolve(HttpServletRequest request);
}
