package com.openclassrooms.chatopapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatopapi.dto.CreateMessageRequest;
import com.openclassrooms.chatopapi.dto.ErrorResponse;
import com.openclassrooms.chatopapi.dto.Response;
import com.openclassrooms.chatopapi.model.Message;
import com.openclassrooms.chatopapi.repository.MessageRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Message", description = "Endpoints for user message")
@RestController
@Validated
@RequestMapping(path = "/messages")
public class MessageController {

	@Autowired
	private MessageRepository messageRepository;

	@Operation(summary = "Send a message", description = "Send a message if authentificated", security = {}, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateMessageRequest.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Message send with success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class), examples = @ExampleObject(value = "{\"message\": \"Message send with success\"}"))),
			@ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Bad request\"}"))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{ \"code\": 401, \"message\": \"Unauthorized\" }"))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "An error has occured"))) })
	@PostMapping(path = "")
	public ResponseEntity<?> createMessage(@Valid @RequestBody CreateMessageRequest messageRequest,
			BindingResult result) {

		Message message = new Message();
		message.setMessage(messageRequest.message);
		message.setUser_id(messageRequest.user_id);
		message.setRental_id(messageRequest.rental_id);
		Message savedMessage = messageRepository.save(message);
		if (savedMessage == null || savedMessage.getId() == null) {
			ErrorResponse err = new ErrorResponse("An error has occured", 500);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
		} else {
			return ResponseEntity.ok(new Response("Message send with success"));
		}
	}

}
