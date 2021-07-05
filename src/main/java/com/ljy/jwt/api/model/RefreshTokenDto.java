package com.ljy.jwt.api.model;

import org.springframework.util.StringUtils;

import com.ljy.jwt.security.JwtToken;

public class RefreshTokenDto {
	private String identifier;
	private String refreshToken;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public boolean isEmpty() {
		return isEmpty(identifier) || isEmpty(refreshToken);
	}
	
	private boolean isEmpty(String value) {
		return !StringUtils.hasText(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof JwtToken) {
			boolean eqIdentifier = ((RefreshTokenDto) obj).identifier.equals(this.identifier);
			boolean eqRefreshToken = ((RefreshTokenDto) obj).refreshToken.equals(this.refreshToken);
			return eqIdentifier && eqRefreshToken;
		}
		return false;
	}

}
