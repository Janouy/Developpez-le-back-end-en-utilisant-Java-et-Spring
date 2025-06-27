package com.openclassrooms.chatopapi.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.chatopapi.dto.CreatedRentalRequest;
import com.openclassrooms.chatopapi.dto.ErrorResponse;
import com.openclassrooms.chatopapi.dto.RentalResponse;
import com.openclassrooms.chatopapi.dto.RentalsListResponse;
import com.openclassrooms.chatopapi.dto.Response;
import com.openclassrooms.chatopapi.dto.UpdateRentalRequest;
import com.openclassrooms.chatopapi.model.Rental;
import com.openclassrooms.chatopapi.repository.RentalRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Rental", description = "Endpoints for rental")
@RestController
@Validated
@RequestMapping(path = "/rentals")
public class RentalsController {

	@Autowired
	private RentalRepository rentalRepository;

	@Operation(summary = "Add a new rental", description = "Creates a new rental.", security = {})
	@RequestBody(required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = CreatedRentalRequest.class)))
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Rental created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class), examples = @ExampleObject("{\"message\": \"Rental created !\"}"))),
			@ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("{\"message\": \"Bad request\"}"))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("{\"code\":401, \"message\":\"Unauthorized\"}"))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("{\"message\":\"An error has occured\"}"))) })
	@PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createRental(@ModelAttribute @Valid CreatedRentalRequest rentalRequest,
			@AuthenticationPrincipal Jwt jwt, BindingResult result) {

		try {
			Long userId = jwt.getClaim("userId");
			Rental rental = new Rental();
			rental.setName(rentalRequest.getName());
			rental.setSurface(rentalRequest.getSurface());
			rental.setPrice(rentalRequest.getPrice());
			rental.setDescription(rentalRequest.getDescription());
			rental.setOwnerId(userId);

			MultipartFile picture = rentalRequest.getPicture();
			if (picture != null && !picture.isEmpty()) {
				String filename = UUID.randomUUID() + "_" + picture.getOriginalFilename();
				Path filePath = Paths.get("uploads", filename);
				Files.createDirectories(filePath.getParent());
				Files.copy(picture.getInputStream(), filePath);
				String publicUrl = "http://localhost:8080/api/uploads/" + filename;
				rental.setPicture(publicUrl);
			} else {
				return ResponseEntity.ok(Map.of("message", "Picture is mandatory"));
			}

			Rental saved = rentalRepository.save(rental);
			if (saved.getId() == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error has occurred");
			}
			return ResponseEntity.ok(new Response("Rental created !"));

		} catch (JwtException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving image : " + e.getMessage());
		}
	}

	@Operation(summary = "Update a rental with its id", description = "Update a rental with its id.", security = @SecurityRequirement(name = "bearerAuth"))
	@RequestBody(required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = UpdateRentalRequest.class)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rental updated with success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class), examples = @ExampleObject("{\"message\": \"Rental updated !\"}"))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Rental not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateRental(@PathVariable Integer id, @Valid @ModelAttribute UpdateRentalRequest request,
			BindingResult result) {
		Optional<Rental> optional = rentalRepository.findById(id);
		if (optional.isEmpty()) {
			ErrorResponse err = new ErrorResponse("Rental not found", 404);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
		}

		Rental rental = optional.get();
		rental.setName(request.getName());
		rental.setSurface(request.getSurface());
		rental.setPrice(request.getPrice());
		rental.setDescription(request.getDescription());

		rentalRepository.save(rental);

		return ResponseEntity.ok(new Response("Rental updated !"));
	}

	@Operation(summary = "Get rentals", description = "Rentals list { rentals: [...] }", security = @SecurityRequirement(name = "bearerAuth"), responses = {
			@ApiResponse(responseCode = "200", description = "Rentals list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentalsListResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("{\"code\":401,\"message\":\"Unauthorized\"}"))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("{\"code\":500,\"message\":\"An error has occured\"}"))) })
	@GetMapping
	public ResponseEntity<RentalsListResponse> getRentals() {
		List<RentalResponse> responseList = StreamSupport.stream(rentalRepository.findAll().spliterator(), false)
				.map(RentalResponse::new).collect(Collectors.toList());
		return ResponseEntity.ok(new RentalsListResponse(responseList));
	}

	@Operation(summary = "Get rental by id", description = "Rental by id", security = @SecurityRequirement(name = "bearerAuth"), responses = {
			@ApiResponse(responseCode = "200", description = "Rental", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentalResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("{\"code\":401,\"message\":\"Unauthorized\"}"))),
			@ApiResponse(responseCode = "404", description = "Rental not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("{\"error\": \"Rental not found\"}"))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("{\"code\":500,\"message\":\"An error has occured\"}"))) })
	@GetMapping("/{id}")
	public ResponseEntity<?> getRental(@PathVariable Integer id) {
		Optional<Rental> optionalRental = rentalRepository.findById(id);

		if (optionalRental.isPresent()) {
			return ResponseEntity.ok(new RentalResponse(optionalRental.get()));
		} else {
			ErrorResponse err = new ErrorResponse("Rental not found", 404);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
		}
	}

}
