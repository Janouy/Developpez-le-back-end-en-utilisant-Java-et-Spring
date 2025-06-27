package com.openclassrooms.chatopapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatopapi.dto.ConnectUserResponse;
import com.openclassrooms.chatopapi.dto.ErrorResponse;
import com.openclassrooms.chatopapi.dto.UserResponse;
import com.openclassrooms.chatopapi.model.User;
import com.openclassrooms.chatopapi.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "Endpoints for user")
@RestController
@Validated
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Operation(summary = "Get authenticated user by id", description = "Returns the authenticated user's information", security = @SecurityRequirement(name = "bearerAuth"), responses = {
			@ApiResponse(responseCode = "200", description = "User info", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConnectUserResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{ \"code\": 401, \"message\": \"Unauthorized\" }"))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{ \"code\": 404, \"message\": \"User not found\" }"))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "An error has occured"))) })
	@GetMapping(path = "/{id}")
	public ResponseEntity<?> getAuthenticatedUserById(@PathVariable Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return ResponseEntity.ok(UserResponse.from(user.get()));
		} else {
			ErrorResponse err = new ErrorResponse("User not found", 404);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
		}

	}

}