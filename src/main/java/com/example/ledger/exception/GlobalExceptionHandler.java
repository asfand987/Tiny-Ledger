package com.example.ledger.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<Map<String, String>> handleInsufficientFunds(InsufficientFundsException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", ex.getMessage()));
	}

}
