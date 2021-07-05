package com.ljy.jwt.api.model;

public class JwtErrorResponse {
	private String msg;
	
	public static JwtErrorResponse create(String msg) {
		return new JwtErrorResponse(msg);
	}
	
	private JwtErrorResponse(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
