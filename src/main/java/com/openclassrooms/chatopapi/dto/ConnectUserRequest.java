package com.openclassrooms.chatopapi.dto;

import jakarta.validation.constraints.NotBlank;


public class ConnectUserRequest {

    @NotBlank(message = "Email is mandatory")
    public String email;

    @NotBlank(message = "Password is mandatory")
    public String password;
}
