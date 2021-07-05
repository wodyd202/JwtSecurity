package com.ljy.jwt;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.ljy.jwt.security.JwtToken;
import com.ljy.jwt.security.JwtTokenStore;

public class TestTokenStore implements JwtTokenStore {
	private final ConcurrentHashMap<String, JwtToken> repo = new ConcurrentHashMap<>();
	
	@Override
	public void save(String userIdentifier, JwtToken token) {
		repo.put(userIdentifier, token);
	}

	@Override
	public Optional<JwtToken> findByUserIdentifier(String userIdentifier) {
		return Optional.ofNullable(repo.get(userIdentifier));
	}

	@Override
	public boolean existByUserIdentifier(String userIdentifier) {
		return findByUserIdentifier(userIdentifier).isPresent();
	}

}
