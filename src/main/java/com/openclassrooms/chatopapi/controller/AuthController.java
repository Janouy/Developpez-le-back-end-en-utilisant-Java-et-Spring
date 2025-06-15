package com.openclassrooms.chatopapi.controller;

import com.openclassrooms.chatopapi.dto.*;
import com.openclassrooms.chatopapi.model.User;
import com.openclassrooms.chatopapi.model.UserResponse;
import com.openclassrooms.chatopapi.repository.UserRepository;
import com.openclassrooms.chatopapi.service.JwtService;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/auth")
public class AuthController {

	@Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtService jwtService;
    
    public AuthController(BCryptPasswordEncoder passwordEncoder, JwtService jwtService, UserRepository userRepository) {
    	this.jwtService = jwtService;
    	this.passwordEncoder = passwordEncoder;
    	this.userRepository = userRepository;
    }

    @PostMapping(path="/register")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.email).isPresent()) {
           return ResponseEntity.badRequest().build();
        }
        User user = new User();
        user.setEmail(userRequest.email);
        user.setName(userRequest.name);
        user.setPassword(passwordEncoder.encode(userRequest.password));

        User savedUser = userRepository.save(user);
        if (savedUser == null || savedUser.getId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error has occured");
        }else {
	        String token = jwtService.generateToken(user);
		    ConnectUserResponse response = new ConnectUserResponse(token);
			return ResponseEntity.ok(response);
        }
    }
    
    @PostMapping(path="/login")
    public ResponseEntity<?> connectUser(@RequestBody ConnectUserRequest userRequest) {
        return userRepository.findByEmail(userRequest.email)
        		.map(user -> {
        			if(passwordEncoder.matches(userRequest.password, user.getPassword())) {
        			    String token = jwtService.generateToken(user);
        			    ConnectUserResponse response = new ConnectUserResponse(token);
        				return ResponseEntity.ok(response);
        			}else {
        				return ResponseEntity.status(404).body(Collections.singletonMap("message", "error"));
        			}
        		})
        		.orElse(ResponseEntity.status(404).body(Collections.singletonMap("message", "error")));
    }
    
    @GetMapping(path="/me")
    public ResponseEntity<?> getAuthenticatedUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            Optional<User> user = jwtService.getUserFromAuthHeader(authHeader);

            if (user.isPresent()) {
                return ResponseEntity
                        .ok(UserResponse.from(user.get()));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Collections.emptyMap());
            }

        } catch (JwtException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.emptyMap());
        }
    }


}
