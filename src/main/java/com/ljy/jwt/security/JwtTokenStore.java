package com.ljy.jwt.security;

import java.util.Optional;

public interface JwtTokenStore {
	void save(String userIdentifier, JwtToken token);
	
	Optional<JwtToken> findByUserIdentifier(String userIdentifier);
	
	boolean existByUserIdentifier(String userIdentifier);
	
	void remove(String userIdentifier);
}
