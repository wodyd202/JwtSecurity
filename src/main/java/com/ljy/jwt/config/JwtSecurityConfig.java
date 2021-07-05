package com.ljy.jwt.config;

import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ljy.jwt.security.JwtAuthenticationFilter;
import com.ljy.jwt.security.JwtAuthenticationToken;
import com.ljy.jwt.security.JwtTokenProvider;
import com.ljy.jwt.security.JwtTokenResolver;
import com.ljy.jwt.security.SimpleJwtTokenResolver;
import com.ljy.jwt.security.JwtTokenStore;

abstract public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtTokenResolver jwtTokenResolver;
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	@Value("${spring.jwt.secretKey}")
	private String secretKey;
	
	@PostConstruct
	protected void encodeSecretKey() {
		this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
	}
	
	protected void customConfigure(HttpSecurity http) throws Exception {};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.httpBasic().disable()
		.csrf().disable()
		.formLogin().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/oauth/token").permitAll()
		.antMatchers(HttpMethod.POST, "/oauth/refresh-token").permitAll()
		.and()
		.exceptionHandling()
		.and()
		.addFilterBefore(new JwtAuthenticationFilter(jwtTokenResolver, jwtAuthenticationToken(), tokenStore, secretKey), UsernamePasswordAuthenticationFilter.class);
		customConfigure(http);
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Bean
	public abstract UserDetailsService userDetailsService();
	
	@Bean
	JwtAuthenticationToken jwtAuthenticationToken() { 
		return new JwtAuthenticationToken(userDetailsService());
	}
	
	@Bean
	JwtTokenProvider jwtTokenProvider() {
		return new JwtTokenProvider(secretKey, tokenStore);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	JwtTokenResolver jwtTokenResolver() {
		return new SimpleJwtTokenResolver();
	}
	
	@Bean
	abstract public JwtTokenStore tokenStore();
}
