package com.ljy.jwt.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jsonwebtoken.Jwts;

public class JwtAuthenticationToken {
	private final UserDetailsService userService;

	public Authentication getAuthentication(final String secretKey, final JwtToken token) {
		UserDetails userDetails = loadUserDetail(secretKey, token);
		return createAuthenticationToken(userDetails);
	}

	private UserDetails loadUserDetail(final String secretKey, final JwtToken token) {
		return userService.loadUserByUsername(getUserIdentifierFromToken(secretKey, token));
	}

	private UsernamePasswordAuthenticationToken createAuthenticationToken(UserDetails userDetails) {
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUserIdentifierFromToken(final String secretKey, final JwtToken token) {
		return Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token.getAccessToken())
					.getBody()
					.getSubject();
	}

	public JwtAuthenticationToken(UserDetailsService userService) {
		this.userService = userService;
	}
}
