package com.ljy.jwt.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface WebSecurityConfigurer {
	void configure(HttpSecurity http) throws Exception;
}
