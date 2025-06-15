package com.openclassrooms.chatopapi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.openclassrooms.chatopapi.dto.CreateMessageRequest;
import com.openclassrooms.chatopapi.model.Message;
import com.openclassrooms.chatopapi.repository.MessageRepository;

@Controller
@RequestMapping(path = "/messages")
public class MessageController {

	@Autowired
	private MessageRepository messageRepository;

	@PostMapping(path = "")
	public ResponseEntity<?> createMessage(@RequestBody CreateMessageRequest messageRequest) {

		Message message = new Message();
		message.setMessage(messageRequest.message);
		message.setUser_id(messageRequest.user_id);
		message.setRental_id(messageRequest.rental_id);

		Message savedMessage = messageRepository.save(message);
		if (savedMessage == null || savedMessage.getId() == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error has occured");
		} else {
			return ResponseEntity.ok(Map.of("message", "Message send with success"));
		}
	}

}
