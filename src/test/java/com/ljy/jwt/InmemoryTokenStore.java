package com.ljy.jwt;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ljy.jwt.security.JwtToken;
import com.ljy.jwt.security.TokenStore;

@Repository
public class InmemoryTokenStore implements TokenStore {

	@Override
	public void save(String userIdentifier, JwtToken token) {
		// TODO Auto-generated method stub
	}

	@Override
	public Optional<JwtToken> findByUserIdentifier(String userIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existByUserIdentifier(String userIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}

}
