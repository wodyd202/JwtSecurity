package com.ljy.jwt.security;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenProvider {
	private final JwtTokenStore tokenStore;
	private final String secretKey;
	
	@Value("${spring.jwt.accessTokenExpireAt}")
	private long accessTokenExpireAt;

	@Value("${spring.jwt.refreshTokenExpireAt}")
	private long refreshTokenExpireAt;

	public JwtToken provideToken(String userIdentifier, List<String> roles) {
		JwtToken token = createJwtToken(userIdentifier, roles);
		tokenStore.save(userIdentifier, token);
		return token;
	}

	private final String ROLE = "ROLE";
	private JwtToken createJwtToken(String userIdentifier, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(userIdentifier);
		claims.put(ROLE, roles);
		
		Date now = new Date();
		String accessToken = createToken(claims, now, new Date(now.getTime() + accessTokenExpireAt));
		String refreshToken = createToken(claims, now, new Date(now.getTime() + refreshTokenExpireAt));
		JwtToken token = new JwtToken(accessToken, refreshToken);
		return token;
	}

	private String createToken(Claims claims, Date now, Date expireDate) {
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(expireDate)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}
	
	public JwtTokenProvider(String secretKey, JwtTokenStore tokenStore) {
		this.secretKey = secretKey;
		this.tokenStore = tokenStore;
	}
}
