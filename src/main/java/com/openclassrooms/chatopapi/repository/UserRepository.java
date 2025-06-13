package com.openclassrooms.chatopapi.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.openclassrooms.chatopapi.model.User;


public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByEmail(String email);
}