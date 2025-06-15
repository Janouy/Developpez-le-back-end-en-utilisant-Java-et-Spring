package com.openclassrooms.chatopapi.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.chatopapi.dto.CreatedRentalRequest;
import com.openclassrooms.chatopapi.dto.RentalResponse;
import com.openclassrooms.chatopapi.dto.UpdateRentalRequest;
import com.openclassrooms.chatopapi.model.Rental;
import com.openclassrooms.chatopapi.model.User;
import com.openclassrooms.chatopapi.model.UserResponse;
import com.openclassrooms.chatopapi.repository.RentalRepository;
import com.openclassrooms.chatopapi.repository.UserRepository;
import com.openclassrooms.chatopapi.service.JwtService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(path="/rentals")
public class RentalsController {
	
	@Autowired
	private RentalRepository rentalRepository;
	private JwtService jwtService;
	
	public RentalsController(JwtService jwtService) {
    	this.jwtService = jwtService;
    }
	
	@PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createRental(
	        @ModelAttribute @Valid CreatedRentalRequest rentalRequest,
	        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

	    try {
	        Optional<User> userOpt = jwtService.getUserFromAuthHeader(authHeader);

	        if (userOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        User user = userOpt.get();             
	        Integer userId = user.getId();          

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
	            String publicUrl = "http://localhost:8080/uploads/" + filename;
	            rental.setPicture(publicUrl);
	        }

	        Rental saved = rentalRepository.save(rental);
	        if (saved.getId() == null) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("An error has occurred");
	        }

	        return ResponseEntity.ok(Map.of(
	            "message", "Rental created !"
	        ));

	    } catch (JwtException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
	    }
	}
	
	   @PutMapping(
	      path = "/{id}",
	      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	    )
	    public ResponseEntity<?> updateRental(
	            @PathVariable Integer id,
	            @Valid @ModelAttribute UpdateRentalRequest request
	    ) {
	        Optional<Rental> optional = rentalRepository.findById(id);
	        if (optional.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "Rental not found with id: " + id));
	        }

	        Rental rental = optional.get();
	        rental.setName(request.getName());
	        rental.setSurface(request.getSurface());
	        rental.setPrice(request.getPrice());
	        rental.setDescription(request.getDescription());

	        Rental updated = rentalRepository.save(rental);

	        return ResponseEntity.ok(Map.of(
	            "message", "Rental updated !"
	        ));
	    }

	
	@GetMapping("")
	public ResponseEntity<?> getRentals() {
	    Iterable<Rental> rentals = rentalRepository.findAll();

	    List<RentalResponse> responseList = new ArrayList<>();
	    for (Rental rental : rentals) {
	        responseList.add(new RentalResponse(rental));
	    }

	    Map<String, Object> responseBody = Map.of("rentals", responseList);
	    return ResponseEntity.ok(responseBody);
	}


	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRental(@PathVariable Integer id) {
	    Optional<Rental> optionalRental = rentalRepository.findById(id);

	    if (optionalRental.isPresent()) {
	        return ResponseEntity.ok(optionalRental.get());
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(Map.of("error", "Rental not found with id: " + id));
	    }
	}


}
