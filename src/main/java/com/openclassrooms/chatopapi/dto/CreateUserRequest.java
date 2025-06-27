package com.openclassrooms.chatopapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email should be valid")
	@Schema(description = "User email", example = "test@test.com", requiredMode = RequiredMode.REQUIRED)
	public String email;

	@NotBlank(message = "Name is mandatory")
	@Size(max = 50, message = "Name must be max 50 characters")
	@Pattern(regexp = "^[\\p{L} '\\-]+$", message = "Name must contain only letters, spaces, apostrophes or hyphens")
	@Schema(description = "Full name of the user", example = "test TEST", requiredMode = RequiredMode.REQUIRED)
	public String name;

	@NotBlank(message = "Password is mandatory")
	@Size(min = 5, max = 25, message = "Password must be min 5 and max 20 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*])[A-Za-z0-9!@#\\$%\\^&\\*]+$", message = "Password must contain at least one digit, one lowercase letter, "
			+ "one uppercase letter and one special character (!@#$%^&*)")
	@Schema(description = "Password", example = "Pass1234*!", requiredMode = RequiredMode.REQUIRED)
	public String password;
}
