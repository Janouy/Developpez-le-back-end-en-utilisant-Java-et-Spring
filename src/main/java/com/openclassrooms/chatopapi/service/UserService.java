package com.openclassrooms.chatopapi.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatopapi.dto.CreateUserRequest;
import com.openclassrooms.chatopapi.model.User;
import com.openclassrooms.chatopapi.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean userExists(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	public User buildUserFromRequest(CreateUserRequest request) {
		User user = new User();
		user.setEmail(request.email);
		user.setName(request.name);
		user.setPassword(passwordEncoder.encode(request.password));
		return user;
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<User> findById(long id) {
		return userRepository.findById(id);
	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
