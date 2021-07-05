package com.ljy.jwt.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ljy.jwt.TestUser;
import com.ljy.jwt.TestUserService;
import com.ljy.jwt.security.JwtToken;
import com.ljy.jwt.security.JwtTokenProvider;
import com.ljy.jwt.security.JwtTokenStore;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AccessTest extends ApiTest{

	@Autowired
	TestUserService userService;
	
	@Autowired
	JwtTokenProvider provider;
	
	@Autowired
	JwtTokenStore tokenStore;
	
	@Value("${spring.jwt.header}")
	private String HEADER;
	
	@Value("${spring.jwt.secretKey}")
	private String secretKey;
	
	@BeforeEach
	void setUp() {
		userService.save(TestUser.create("identifier", "password"));
		provider.provideToken("identifier", new ArrayList<>());
	}
	
	@Test
	@DisplayName("accessToken이 없을 때 실패")
	void noAccessToken() throws Exception {
		mvc.perform(get("/authenticate"))
			.andDo(print())
			.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("accessToken이 유효하지 않을 때")
	void invalidAccessToken() throws Exception {
		mvc.perform(get("/authenticate")
					.header(HEADER, "fail"))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@DisplayName("정상 요청")
	void success() throws Exception {
		JwtToken jwtToken = tokenStore.findByUserIdentifier("identifier").get();
		
		mvc.perform(get("/authenticate")
					.header(HEADER, jwtToken.getAccessToken()))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("토큰 만료로 인한 엑세스 거부")
	void accessDenied() throws Exception {
		String accessToken = Jwts.builder()
			.setIssuedAt(new Date())
			.setExpiration(new Date(new Date().getTime() - 10_000_000))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
		tokenStore.save("identifier", new JwtToken(accessToken));
		
		JwtToken jwtToken = tokenStore.findByUserIdentifier("identifier").get();
		
		mvc.perform(get("/authenticate")
					.header(HEADER, jwtToken.getAccessToken()))
		.andExpect(status().isForbidden());
	}
}
