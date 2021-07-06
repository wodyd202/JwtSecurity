package com.ljy.jwt.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ljy.jwt.exception.InvalidJwtTokenException;
import com.ljy.jwt.security.JwtToken;
import com.ljy.jwt.security.JwtToken.JwtTokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenTest {
	
	@Test
	@DisplayName("알 수 없는 토큰")
	void invalidToken() {
		JwtToken token = new JwtToken("invalidToken");
		assertThrows(InvalidJwtTokenException.class, ()->{
			token.validation("secretKey", JwtTokenType.ACCESS_TOKEN);
		});
	}
	
	
	@Test
	@DisplayName("토큰 값에서 사용자 아이디 추출")
	void getUserIdentifier() {
		Claims claims = Jwts.claims().setSubject("userIdentifier");
		
		String accessToken = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date())
			.setExpiration(new Date(new Date().getTime() + 10_000_000))
			.signWith(SignatureAlgorithm.HS256, "secretKey")
			.compact();
		
		String userIdentifier = Jwts.parser()
			.setSigningKey("secretKey")
			.parseClaimsJws(accessToken)
			.getBody()
			.getSubject();
		
		assertThat(userIdentifier.equals("userIdentifier")).isTrue();
	}
	
	@Test
	@DisplayName("같은 토큰값인가 체크")
	void equals() {
		JwtToken jwtToken_1 = new JwtToken(Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 10_000_000))
				.signWith(SignatureAlgorithm.HS256, "secretKey")
				.compact());

		Object jwtToken_2 = new JwtToken(jwtToken_1.getAccessToken());

		assertThat(jwtToken_1.equals(jwtToken_2)).isTrue();
	}
	
	@Test
	@DisplayName("토큰 만료")
	void expire() {
		String accessToken = Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() - 10_000_000))
				.signWith(SignatureAlgorithm.HS256, "secretKey")
				.compact();
		
		JwtToken token = new JwtToken(accessToken);
		assertThrows(ExpiredJwtException.class, ()->{
			token.validation("secretKey", JwtTokenType.ACCESS_TOKEN);
		});
	}
}
