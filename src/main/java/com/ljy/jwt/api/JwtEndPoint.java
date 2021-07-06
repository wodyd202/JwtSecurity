package com.ljy.jwt.api;

import org.springframework.http.ResponseEntity;

import com.ljy.jwt.api.model.AccessTokenDto;
import com.ljy.jwt.api.model.RefreshTokenDto;
import com.ljy.jwt.security.JwtToken;

public interface JwtEndPoint {
	ResponseEntity<JwtToken> oauthToken(AccessTokenDto loginInfo);
	ResponseEntity<JwtToken> refreshToken(RefreshTokenDto refreshToken);
}
