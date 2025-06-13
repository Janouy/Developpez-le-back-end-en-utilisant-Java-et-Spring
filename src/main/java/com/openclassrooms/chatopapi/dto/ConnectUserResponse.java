package com.openclassrooms.chatopapi.dto;


public class ConnectUserResponse {
    public String jwt;
  

    public ConnectUserResponse(String token) {
        this.jwt = token;
    }
}
