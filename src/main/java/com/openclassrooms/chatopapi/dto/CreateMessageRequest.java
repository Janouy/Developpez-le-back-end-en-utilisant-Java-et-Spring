package com.openclassrooms.chatopapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateMessageRequest {

	@NotBlank(message = "Message is mandatory")
	@Schema(description = "Message", example = "New message for user...", requiredMode = RequiredMode.REQUIRED)
	public String message;

	@Valid
	@NotNull(message = "User id is mandatory")
	@Min(value = 1, message = "User id must be greater than 0")
	public Integer user_id;

	@Valid
	@NotNull(message = "Rental id is mandatory")
	@Min(value = 1, message = "Rental id must be greater than 0")
	public Integer rental_id;

}
