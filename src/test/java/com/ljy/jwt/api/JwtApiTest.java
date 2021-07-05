package com.ljy.jwt.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ljy.jwt.TestUser;
import com.ljy.jwt.TestUserService;

public class JwtApiTest extends ApiTest {

	@Value("${spring.jwt.accessToken.empty.errorMsg}")
	private String ACCESS_TOKEN_EMPTY_ERROR_MESSAGE;

	@Value("${spring.jwt.accessToken.invalid.errorMsg}")
	private String ACCESS_TOKEN_INVALID_ERROR_MESSAGE;
	
	@Autowired
	TestUserService userService;
	
	@Test
	@DisplayName("accessToken 요청시 userIdentifier가 비워져 있는 경우 실패")
	void isEmptyIdentifierWhenAccessToken() throws Exception {
		mvc.perform(post("/oauth/token")
					.param("password", "password"))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.msg").value(ACCESS_TOKEN_EMPTY_ERROR_MESSAGE));
	}
	
	@Test
	@DisplayName("accessToken 요청시 password가 비워져 있는 경우 실패")
	void isEmptyPasswordWhenAccessToken() throws Exception {
		mvc.perform(post("/oauth/token")
				.param("identifier", ""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").value(ACCESS_TOKEN_EMPTY_ERROR_MESSAGE));
	}
	
	@Test
	@DisplayName("accessToken 요청시 모든값을 입력하지 않았을때 실패")
	void isEmptyAllWhenAccessToken() throws Exception {
		mvc.perform(post("/oauth/token"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").value(ACCESS_TOKEN_EMPTY_ERROR_MESSAGE));
	}

	@Nested
	@DisplayName("accessToken을 정상적으로 입력했을 때")
	class ValidAccessToken {

		@BeforeEach
		void setUp() {
			userService.save(TestUser.create("identifier", "password"));
		}
		
		@Test
		@DisplayName("아이디 비밀번호 불일치")
		void fail() throws Exception {
			mvc.perform(post("/oauth/token")
					.param("identifier", "fail")
					.param("password", "fail"))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.msg").value(ACCESS_TOKEN_INVALID_ERROR_MESSAGE));
		}
		
		@Test
		@DisplayName("토큰 발급")
		void success() throws Exception {
			mvc.perform(post("/oauth/token")
					.param("identifier", "identifier")
					.param("password", "password"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.accessToken").exists())
					.andExpect(jsonPath("$.refreshToken").exists());
		}
	}
	
	@Value("${spring.jwt.refreshToken.empty.errorMsg}")
	private String REFRESH_TOKEN_EMPTY_ERROR_MESSAGE;

	@Value("${spring.jwt.refreshToken.notExist.errorMsg}")
	private String REFRESH_TOKEN_NOT_EXIST_ERROR_MESSAGE;

	@Value("${spring.jwt.refreshToken.invalid.errorMsg}")
	private String REFRESH_TOKEN_INVALID_ERROR_MESSAGE;

	@Test
	@DisplayName("refreshToken 요청시 refreshToken이 비워져 있는 경우 실패")
	void isEmptyRefreshTokenWhenRefreshToken() throws Exception {
		mvc.perform(post("/oauth/refresh-token")
				.param("identifier", "identifier"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").value(REFRESH_TOKEN_EMPTY_ERROR_MESSAGE));
	}
	
	@Test
	@DisplayName("refreshToken 요청시 userIdentifier가 비워져 있는 경우 실패")
	void isEmptyIdentifierWhenRefreshToken() throws Exception {
		mvc.perform(post("/oauth/refresh-token")
				.param("refreshToken", "refreshToken"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").value(REFRESH_TOKEN_EMPTY_ERROR_MESSAGE));
	}

	@Test
	@DisplayName("refreshToken 요청시  입력하지 않았을 때 실패")
	void isEmptyAllWhenRefreshToken() throws Exception {
		mvc.perform(post("/oauth/refresh-token"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.msg").value(REFRESH_TOKEN_EMPTY_ERROR_MESSAGE));
	}
	
}
