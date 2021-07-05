package com.ljy.jwt.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

public class JwtApiTest extends ApiTest {
	
	@Test
	void accessToken_요청시_userIdentifier가_비워져_있는_경우_실패() throws Exception {
		mvc.perform(post("/oauth/token")
					.param("identifier", "")
					.param("password", "password"))
					.andDo(print())
					.andExpect(status().isBadRequest());
	}
}
