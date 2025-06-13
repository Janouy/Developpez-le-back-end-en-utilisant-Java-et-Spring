package com.openclassrooms.chatopapi.controller;

import com.openclassrooms.chatopapi.repository.UserRepository;
import com.openclassrooms.chatopapi.model.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(path="/user")
public class UserController {
  @Autowired 
  private UserRepository userRepository;

  @GetMapping(path="/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Integer id) {
      Optional<User> user = userRepository.findById(id);
      return user.map(ResponseEntity::ok)
                 .orElseGet(() -> ResponseEntity.notFound().build());
  }

}