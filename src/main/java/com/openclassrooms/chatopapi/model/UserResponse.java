package com.openclassrooms.chatopapi.model;

import java.time.LocalDateTime;



public class UserResponse {
    public Integer id;
    public String name;
    public String email;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

  
    
    public UserResponse(Integer id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }
    

}
