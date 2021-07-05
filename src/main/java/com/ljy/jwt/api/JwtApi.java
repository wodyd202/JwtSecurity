package com.ljy.jwt.api;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ljy.jwt.api.model.AccessTokenDto;
import com.ljy.jwt.api.model.RefreshTokenDto;
import com.ljy.jwt.exception.InvalidAccessTokenException;
import com.ljy.jwt.exception.InvalidRefreshTokenException;
import com.ljy.jwt.security.JwtToken;
import com.ljy.jwt.security.JwtTokenProvider;
import com.ljy.jwt.security.JwtTokenStore;

@RestController
@RequestMapping("oauth")
public class JwtApi {
	
	@Autowired private UserDetailsService userService;
	@Autowired private JwtTokenProvider jwtTokenProvider;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private JwtTokenStore tokenStore;
	
	@Value("${spring.jwt.refreshToken.empty.errorMsg}")
	private String REFRESH_TOKEN_EMPTY_ERROR_MESSAGE;

	@Value("${spring.jwt.refreshToken.notExist.errorMsg}")
	private String REFRESH_TOKEN_NOT_EXIST_ERROR_MESSAGE;

	@Value("${spring.jwt.refreshToken.invalid.errorMsg}")
	private String REFRESH_TOKEN_INVALID_ERROR_MESSAGE;
	
	@PostMapping("refresh-token")
	public ResponseEntity<JwtToken> refreshToken(RefreshTokenDto refreshToken) {
		if(refreshToken.isEmpty()) {
			throw new InvalidRefreshTokenException(REFRESH_TOKEN_EMPTY_ERROR_MESSAGE);
		}

		JwtToken jwtToken = tokenStore.findByUserIdentifier(refreshToken.getIdentifier())
									   .orElseThrow(() -> new InvalidRefreshTokenException(REFRESH_TOKEN_NOT_EXIST_ERROR_MESSAGE));

		if(notEqualRefreshToken(refreshToken, jwtToken)) {
			throw new InvalidRefreshTokenException(REFRESH_TOKEN_INVALID_ERROR_MESSAGE);
		}
		
		UserDetails loginUser = getPersistedUser(refreshToken.getIdentifier());
		return new ResponseEntity<>(createToken(loginUser), HttpStatus.OK);
	}

	private boolean notEqualRefreshToken(RefreshTokenDto refreshToken, JwtToken jwtToken) {
		return !jwtToken.equalRefreshToken(refreshToken.getRefreshToken());
	}


	@Value("${spring.jwt.accessToken.empty.errorMsg}")
	private String ACCESS_TOKEN_EMPTY_ERROR_MESSAGE;

	@Value("${spring.jwt.accessToken.invalid.errorMsg}")
	private String ACCESS_TOKEN_INVALID_ERROR_MESSAGE;
	
	@PostMapping("token")
	public ResponseEntity<JwtToken> oauthToken(AccessTokenDto loginInfo) {
		if(isEmpty(loginInfo.getIdentifier()) || isEmpty(loginInfo.getPassword())) {
			throw new InvalidAccessTokenException(ACCESS_TOKEN_EMPTY_ERROR_MESSAGE);
		}
		
		UserDetails loginUser = getPersistedUser(loginInfo.getIdentifier());
		if (notMatchPassword(loginInfo, loginUser)) {
			throw new InvalidAccessTokenException(ACCESS_TOKEN_INVALID_ERROR_MESSAGE);
		} 
		return new ResponseEntity<>(createToken(loginUser), HttpStatus.OK);
	}

	private UserDetails getPersistedUser(String identifier) {
		return userService.loadUserByUsername(identifier);
	}

	private boolean isEmpty(String identifier) {
		return !StringUtils.hasText(identifier);
	}

	private boolean notMatchPassword(AccessTokenDto loginInfo, UserDetails loginUser) {
		return !passwordEncoder.matches(loginInfo.getPassword(), loginUser.getPassword());
	}

	private JwtToken createToken(UserDetails loginUser) {
		return jwtTokenProvider.provideToken(loginUser.getUsername(), loginUser.getAuthorities()
							   .stream()
							   .map(c -> new String(c.getAuthority()))
							   .collect(Collectors.toList()));
	}
}
