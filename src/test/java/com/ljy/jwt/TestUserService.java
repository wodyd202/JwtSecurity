package com.ljy.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestUserService implements UserDetailsService {
	private final List<TestUser> repo = new ArrayList<>();
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${spring.jwt.accessToken.invalid.errorMsg}")
	private String ACCESS_TOKEN_INVALID_ERROR_MESSAGE;
	
	public void save(TestUser user) {
		user.encodePassword(passwordEncoder);
		repo.add(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		for(TestUser user : repo) {
			if(user.getIdentifier().equals(username)) {
				return new TestUserPrincipal(user);
			}
		}
		throw new UsernameNotFoundException(ACCESS_TOKEN_INVALID_ERROR_MESSAGE);
	}

}
