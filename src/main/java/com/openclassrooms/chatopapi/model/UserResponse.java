package com.openclassrooms.chatopapi.model;


public class UserResponse {
    public Integer id;
    public String name;
    public String email;

    public UserResponse(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
