package com.ljy.jwt.security;

import java.util.Date;

import com.ljy.jwt.exception.InvalidJwtTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JwtToken {
	public enum JwtTokenType { ACCESS_TOKEN, REFRESH_TOKEN }
	
	private String accessToken;
	private String refreshToken;

	public String getAccessToken() {
		return accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof JwtToken) {
			return ((JwtToken) obj).accessToken.equals(this.accessToken);
		}
		return false;
	}
	
	public boolean equalRefreshToken(String refreshToken) {
		return this.refreshToken.equals(refreshToken);
	}
	
	JwtToken() {}
	
	public JwtToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public JwtToken(String accessToken,String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public void validation(String secretKey, JwtTokenType type) {
		if(expireToken(secretKey, type)) {
			throw new InvalidJwtTokenException();
		}
	}

	private boolean expireToken(String secretKey, JwtTokenType type) {
		try {
			Jws<Claims> claims = Jwts.parser()
										.setSigningKey(secretKey)
										.parseClaimsJws(type.equals(JwtTokenType.ACCESS_TOKEN) ? accessToken : refreshToken);
			boolean remainExpiration = claims.getBody().getExpiration().before(new Date());
			return remainExpiration;
		}catch (Exception e) {
			throw new InvalidJwtTokenException();
		}
	}

}
