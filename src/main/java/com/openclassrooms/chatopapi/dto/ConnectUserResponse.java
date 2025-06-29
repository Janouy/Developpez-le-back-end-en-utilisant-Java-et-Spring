package com.openclassrooms.chatopapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ConnectUserResponse {

	@Schema(description = "Json web token", example = "eyJhbGc...")
	public String token;

	public ConnectUserResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
