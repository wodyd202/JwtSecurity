package com.ljy.jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ljy.jwt.config.JwtSecurityConfig;
import com.ljy.jwt.security.JwtTokenStore;

@Configuration
@EnableWebSecurity
public class TestConfiguration extends JwtSecurityConfig {

	@Override
	protected void customConfigure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/authenticate").authenticated()
			.antMatchers(HttpMethod.GET, "/permit-all").permitAll();
			
	}
	
	@Override
	public JwtTokenStore tokenStore() {
		return new TestTokenStore();
	}

	@Override
	public UserDetailsService userDetailsService() {
		return new TestUserService();
	}

}
