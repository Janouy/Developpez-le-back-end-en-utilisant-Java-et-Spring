package com.openclassrooms.chatopapi.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    public Integer id;
    public String name;
    public String email;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    
    public UserResponse(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    public UserResponse(Integer id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }
    
    public static UserResponse fromAuth(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

}
