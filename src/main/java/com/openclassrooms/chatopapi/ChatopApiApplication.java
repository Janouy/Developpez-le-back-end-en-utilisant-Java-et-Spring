package com.openclassrooms.chatopapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ChatopApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
		System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("JWT_KEY", dotenv.get("JWT_KEY"));
		System.setProperty("APP_BASE_URL", dotenv.get("APP_BASE_URL"));
		SpringApplication.run(ChatopApiApplication.class, args);
	}

}
