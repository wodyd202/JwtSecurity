package com.ljy.jwt.security;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InmemoryTokenStore implements JwtTokenStore {
	private final ConcurrentHashMap<String, JwtToken> userRepo = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, String> tokenUserRepo = new ConcurrentHashMap<>();
	
	@Override
	public void save(String userIdentifier, JwtToken token) {
		userRepo.put(userIdentifier, token);
		tokenUserRepo.put(token.getAccessToken(), userIdentifier);
	}

	@Override
	public Optional<JwtToken> findByUserIdentifier(String userIdentifier) {
		return Optional.ofNullable(userRepo.get(userIdentifier));
	}

	@Override
	public boolean existByUserIdentifier(String userIdentifier) {
		return findByUserIdentifier(userIdentifier).isPresent();
	}

	@Override
	public void remove(JwtToken token) {
		String userIdentifier = tokenUserRepo.get(token.getAccessToken());
		userRepo.remove(userIdentifier);
		tokenUserRepo.remove(token.getAccessToken());
	}
}
