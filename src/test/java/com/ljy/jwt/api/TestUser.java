package com.ljy.jwt.api;

import org.springframework.security.crypto.password.PasswordEncoder;

public class TestUser {
	private String identifier;
	private String password;

	public static TestUser create(String identifier, String password) {
		return new TestUser(identifier, password);
	}
	
	public void encodePassword(PasswordEncoder passwordEncoder) {
		password = passwordEncoder.encode(password);
	}
	
	TestUser(String identifier, String password) {
		this.identifier = identifier;
		this.password = password;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getPassword() {
		return password;
	}
}
