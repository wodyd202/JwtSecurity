package com.ljy.jwt.security;

import com.ljy.jwt.exception.InvalidJwtTokenException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

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
		try {
			Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(type.equals(JwtTokenType.ACCESS_TOKEN) ? accessToken : refreshToken);
		}catch (MalformedJwtException e) {
			throw new InvalidJwtTokenException();
		}catch (ExpiredJwtException e) {
			throw e;
		}catch (SignatureException e) {
			throw new InvalidJwtTokenException();
		}
	}
}
