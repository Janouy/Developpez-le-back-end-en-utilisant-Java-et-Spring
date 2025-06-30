package com.openclassrooms.chatopapi.dto;

import java.time.LocalDateTime;

import com.openclassrooms.chatopapi.model.User;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {
	@Schema(description = "Unique identifier of the user", example = "1")
	public Long id;

	@Schema(description = "Full name of the user", example = "Owner Name")
	public String name;

	@Schema(description = "Email address of the user", example = "test@test.com")
	public String email;

	@Schema(description = "Timestamp when the user was created (ISO-8601 format)", example = "2022-02-02T14:30:00")
	public LocalDateTime createdAt;

	@Schema(description = "Timestamp of the last update to the user record (ISO-8601 format)", example = "2022-08-02T09:15:45")
	public LocalDateTime updatedAt;

	public UserResponse(Long id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static UserResponse from(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(),
				user.getUpdatedAt());
	}

}
