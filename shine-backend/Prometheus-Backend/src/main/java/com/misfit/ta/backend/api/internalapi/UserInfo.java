package com.misfit.ta.backend.api.internalapi;

public class UserInfo {
	private String token;
	private String userId;
	private String email;
	private String password;
	public UserInfo(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public UserInfo() {
		
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
