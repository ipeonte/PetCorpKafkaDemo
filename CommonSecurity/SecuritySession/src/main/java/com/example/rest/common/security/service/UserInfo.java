package com.example.rest.common.security.service;

public class UserInfo {

	private String name;
	
	private String mask;
	
	/**
	 * Default constructor
	 */
	public UserInfo() {}

	public UserInfo(String name, String mask) {
		this.name = name;
		this.mask = mask;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String getMask() {
		return mask;
	}
}
