package com.ljy.jwt.api.model;

public class JwtErrorResponse {
	private String msg;
	
	public JwtErrorResponse(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
