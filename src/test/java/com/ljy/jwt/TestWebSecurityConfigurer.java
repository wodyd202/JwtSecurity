package com.ljy.jwt;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ljy.jwt.api.TestUserService;
import com.ljy.jwt.config.JwtSecurityConfiguration;
import com.ljy.jwt.security.InmemoryTokenStore;
import com.ljy.jwt.security.JwtTokenStore;

@EnableWebSecurity
public class TestWebSecurityConfigurer extends JwtSecurityConfiguration {

	@Override
	protected void customConfigure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/permit-all").permitAll()
			.antMatchers("/authenticate").authenticated();
	}
	
	@Override
	protected UserDetailsService userDetailsService() {
		return new TestUserService();
	}

	@Override
	public JwtTokenStore tokenStore() {
		return new InmemoryTokenStore();
	}
}
