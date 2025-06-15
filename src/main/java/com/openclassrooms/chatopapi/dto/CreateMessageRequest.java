package com.openclassrooms.chatopapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateMessageRequest {

	@NotBlank(message = "Message is mandatory")
	public String message;

	@NotNull(message = "User id is mandatory")
	@Min(value = 1, message = "User id must be greater than 0")
	public Integer user_id;

	@NotNull(message = "Rental id is mandatory")
	@Min(value = 1, message = "Rental id must be greater than 0")
	public Integer rental_id;

}
