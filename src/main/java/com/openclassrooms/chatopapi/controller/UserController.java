package com.openclassrooms.chatopapi.controller;

import com.openclassrooms.chatopapi.service.JwtService;
import com.openclassrooms.chatopapi.model.User;
import com.openclassrooms.chatopapi.model.UserResponse;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(path="/user")
public class UserController {
	
  @Autowired 
  private JwtService jwtService;
  

  @GetMapping(path="/{id}")
  public ResponseEntity<UserResponse> getAuthenticatedUserById(@RequestHeader(HttpHeaders.AUTHORIZATION)String authHeader, @PathVariable Integer id) {
      try {
        	Optional<User> user = jwtService.getUserFromAuthHeader(authHeader);

            if(user.isPresent()) {
            	return ResponseEntity.ok(UserResponse.from(user.get()));
            }else {
            	   return ResponseEntity.notFound().build();
            }
         
        } catch (JwtException e) {
            return ResponseEntity.status(401).build();
        }
     
  }

}