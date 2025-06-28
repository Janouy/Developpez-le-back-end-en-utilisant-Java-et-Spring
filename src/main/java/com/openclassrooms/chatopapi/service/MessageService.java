package com.openclassrooms.chatopapi.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.chatopapi.dto.CreateMessageRequest;
import com.openclassrooms.chatopapi.model.Message;
import com.openclassrooms.chatopapi.repository.MessageRepository;

@Service
public class MessageService {

	private final MessageRepository messageRepository;

	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public Message buildMessageFromRequest(CreateMessageRequest request) {
		Message message = new Message();
		message.setMessage(request.message);
		message.setUser_id(request.user_id);
		message.setRental_id(request.rental_id);
		return message;
	}

	public Message saveMessage(Message message) {
		return messageRepository.save(message);
	}

}
