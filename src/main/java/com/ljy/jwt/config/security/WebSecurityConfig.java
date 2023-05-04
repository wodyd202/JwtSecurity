package com.ljy.jwt.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.jwt.config.security.filter.JwtAuthenticationFilter;
import com.ljy.jwt.config.security.filter.RefreshTokenFilter;
import com.ljy.jwt.config.security.filter.handler.TokenFailureHandler;
import com.ljy.jwt.config.security.filter.AccessTokenFilter;
import com.ljy.jwt.config.security.filter.handler.TokenSuccessHandler;
import com.ljy.jwt.config.security.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    public WebSecurityConfig(JwtUtils jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtUtils = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers(POST, "/api/v1/users").permitAll()
            .antMatchers(GET, "/api/v1/users").authenticated()
        .and()
        .addFilterBefore(accessTokenFilter(), LogoutFilter.class)
        .addFilterBefore(refreshTokenFilter(), LogoutFilter.class)
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public AccessTokenFilter accessTokenFilter() throws Exception {
        AccessTokenFilter filter = new AccessTokenFilter("/api/v1/login", POST);
        filter.setAuthenticationManager(this.authenticationManager());
        filter.setAuthenticationSuccessHandler(new TokenSuccessHandler(jwtUtils, objectMapper));
        filter.setAuthenticationFailureHandler(new TokenFailureHandler(objectMapper));
        return filter;
    }

    @Bean
    public RefreshTokenFilter refreshTokenFilter() throws Exception {
        RefreshTokenFilter filter = new RefreshTokenFilter("/api/v1/refresh", POST, jwtUtils);
        filter.setAuthenticationManager(this.authenticationManager());
        filter.setAuthenticationSuccessHandler(new TokenSuccessHandler(jwtUtils, objectMapper));
        filter.setAuthenticationFailureHandler(new TokenFailureHandler(objectMapper));
        return filter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils);
    }
}
