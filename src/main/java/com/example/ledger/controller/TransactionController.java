package com.example.ledger.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ledger.dto.BalanceResponse;
import com.example.ledger.dto.CreateTransactionRequest;
import com.example.ledger.model.Transaction;
import com.example.ledger.service.LedgerService;

import jakarta.validation.Valid;

@RestController
public class TransactionController {

	private final LedgerService ledgerService;

	public TransactionController(LedgerService ledgerService) {
		this.ledgerService = ledgerService;
	}

	@PostMapping("/transactions")
	@ResponseStatus(HttpStatus.CREATED)
	public Transaction createTransaction(@Valid @RequestBody CreateTransactionRequest request) {
		return ledgerService.recordTransaction(request);
	}

	@GetMapping("/balance")
	public BalanceResponse getBalance() {
		return new BalanceResponse(ledgerService.getBalance());
	}

	@GetMapping("/transactions")
	public List<Transaction> getTransactions() {
		return ledgerService.getTransactionHistory();
	}

}
