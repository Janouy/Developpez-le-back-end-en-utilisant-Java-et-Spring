package com.openclassrooms.chatopapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatopapi.dto.ConnectUserRequest;
import com.openclassrooms.chatopapi.dto.ConnectUserResponse;
import com.openclassrooms.chatopapi.dto.CreateUserRequest;
import com.openclassrooms.chatopapi.dto.ErrorResponse;
import com.openclassrooms.chatopapi.dto.UserResponse;
import com.openclassrooms.chatopapi.model.User;
import com.openclassrooms.chatopapi.service.JwtService;
import com.openclassrooms.chatopapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "Endpoints for user registration, login, and profile access")
@RestController
@Validated
@RequestMapping(path = "/auth")
public class AuthController {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserService userService;

	@Operation(summary = "Register a new user", description = "Creates a new user and returns a JWT token on success.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequest.class))), responses = {
			@ApiResponse(responseCode = "200", description = "User registered successfully, returns JWT token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConnectUserResponse.class))),
			@ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Bad request\"}"))),
			@ApiResponse(responseCode = "409", description = "User already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"code\": \"409\",\"message\": \"User already exists\"}"))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"code\": \"500\",\"message\": \"An error has occured\"}"))) })
	@SecurityRequirements({})
	@PostMapping(path = "/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest userRequest, BindingResult result) {

		if (userService.userExists(userRequest.email)) {
			ErrorResponse err = new ErrorResponse("User already exists", 409);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
		}
		User user = userService.buildUserFromRequest(userRequest);
		User savedUser = userService.saveUser(user);

		if (savedUser == null || savedUser.getId() == null) {
			ErrorResponse err = new ErrorResponse("An error has occured", 500);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
		}

		String token = jwtService.generateToken(user);
		ConnectUserResponse response = new ConnectUserResponse(token);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Login user\", description = \"Authenticates the user and returns a JWT token.", description = "User is login.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConnectUserRequest.class), examples = @ExampleObject(value = "{\"email\": \"test@test.com\", \"password\": \"test!31\"}"))), responses = {
			@ApiResponse(responseCode = "200", description = "User registered successfully, returns JWT token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConnectUserResponse.class))),
			@ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Bad request\"}"))),
			@ApiResponse(responseCode = "401", description = "Wrong login infos (password or emai)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{ \"code\": 401, \"message\": \"Invalid credentials\" }"))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "An error has occured"))) })
	@SecurityRequirements({})
	@PostMapping(path = "/login")
	public ResponseEntity<?> connectUser(@RequestBody ConnectUserRequest userRequest) {
		return userService.findByEmail(userRequest.email).map(user -> {
			if (userService.checkPassword(userRequest.password, user.getPassword())) {
				String token = jwtService.generateToken(user);
				ConnectUserResponse response = new ConnectUserResponse(token);
				return ResponseEntity.ok(response);
			} else {
				ErrorResponse err = new ErrorResponse("Invalid credentials", 401);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
			}
		}).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid credentials", 401)));
	}

	@Operation(summary = "Get authenticated user by jwt", description = "Returns the authenticated user's information", security = @SecurityRequirement(name = "bearerAuth"), responses = {
			@ApiResponse(responseCode = "200", description = "User info", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"1\", \"name\": \"test TEST\", \"email\": \"test@test.com\", \"created_at\": \"2022/02/02\", \"updated_at\": \"2022/08/02 \"}"))),
			@ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Bad request\"}"))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{ \"code\": 401, \"message\": \"Unauthorized\" }"))),
			@ApiResponse(responseCode = "404", description = "User nor found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{ \"code\": 404, \"message\": \"User not found\" }"))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "An error has occured"))) })

	@GetMapping(path = "/me")
	public ResponseEntity<?> me(@AuthenticationPrincipal Jwt jwt) {
		Long userId = jwt.getClaim("userId");
		Optional<User> user = userService.findById(userId);
		if (user.isPresent()) {
			return ResponseEntity.ok(UserResponse.from(user.get()));
		} else {
			ErrorResponse err = new ErrorResponse("User not found", 404);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
		}
	}

}
