package com.openclassrooms.chatopapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.openclassrooms.chatopapi.model.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {

}