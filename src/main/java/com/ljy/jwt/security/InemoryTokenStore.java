package com.ljy.jwt.security;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InemoryTokenStore implements JwtTokenStore {
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
