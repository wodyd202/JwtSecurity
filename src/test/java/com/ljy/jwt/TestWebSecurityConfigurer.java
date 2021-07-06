package com.ljy.jwt;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

import com.ljy.jwt.config.WebSecurityConfigurer;

@Component
public class TestWebSecurityConfigurer implements WebSecurityConfigurer {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/authenticate").authenticated()
			.antMatchers(HttpMethod.GET, "/permit-all").permitAll();
	}
}
