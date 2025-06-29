package com.openclassrooms.chatopapi.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for validation-related errors in REST controllers.
 *
 * This class uses @RestControllerAdvice to intercept and handle specific
 * exceptions commonly thrown during request validation or JSON parsing.
 */
@RestControllerAdvice
public class ValidationExceptionHandler {

	private static final String GENERIC_MSG = "Bad request";

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleConstraintViolation(ConstraintViolationException ex) {
		return Map.of("message", GENERIC_MSG);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		return Map.of("message", GENERIC_MSG);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleInvalidJson(HttpMessageNotReadableException ex) {
		return Map.of("message", GENERIC_MSG);
	}
}
