package com.ljy.jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ljy.jwt.config.JwtSecurityConfig;
import com.ljy.jwt.security.TokenStore;

@Configuration
@EnableWebSecurity
public class TestConfiguration extends JwtSecurityConfig {

	@Override
	public TokenStore tokenStore() {
		return new TestTokenStore();
	}

	@Override
	public UserDetailsService userDetailsService() {
		return new TestUserService();
	}

}
