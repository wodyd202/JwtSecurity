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
	
	@Test
	@DisplayName("토큰 재발급 후 이전 토큰으로 엑세스하면 엑세스 거부")
	void refreshTokenAfterBeforeAccessTokenAccess() throws Exception {
		JwtToken jwtToken = tokenStore.findByUserIdentifier("identifier").get();
	
		Thread.sleep(1000);
		provider.provideToken("identifier", new ArrayList<>());
		
		mvc.perform(get("/authenticate")
				.header(HEADER, jwtToken.getAccessToken()))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@DisplayName("토큰 재발급 후 재발급한 토큰으로 요청")
	void refreshTokenAfterAccess() throws Exception {
		provider.provideToken("identifier", new ArrayList<>());
		
		JwtToken jwtToken = tokenStore.findByUserIdentifier("identifier").get();
		
		mvc.perform(get("/authenticate")
				.header(HEADER, jwtToken.getAccessToken()))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("모든 사용자가 접근할 수 있는 페이지는 토큰 유무를 상관하지 않음")
	void permitAll() throws Exception {
		mvc.perform(get("/permit-all")
				.header(HEADER, "sasa"))
		.andExpect(status().isNotFound());

		mvc.perform(get("/permit-all"))
		.andExpect(status().isNotFound());
	}
	
}