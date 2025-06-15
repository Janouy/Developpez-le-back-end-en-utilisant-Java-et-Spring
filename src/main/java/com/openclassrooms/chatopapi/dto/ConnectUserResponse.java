package com.openclassrooms.chatopapi.dto;


public class ConnectUserResponse {
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
