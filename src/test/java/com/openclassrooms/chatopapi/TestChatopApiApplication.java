package com.openclassrooms.chatopapi;

import org.springframework.boot.SpringApplication;

public class TestChatopApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(ChatopApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
