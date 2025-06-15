package com.openclassrooms.chatopapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
	
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    public String email;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be max 50 characters")
    public String name;

    @NotBlank(message = "Password is mandatory")
    public String password;
}
