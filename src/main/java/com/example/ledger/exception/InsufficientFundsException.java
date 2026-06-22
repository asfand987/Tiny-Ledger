package com.example.ledger.exception;

public class InsufficientFundsException extends RuntimeException {

	public InsufficientFundsException() {
		super("Insufficient funds");
	}

}
