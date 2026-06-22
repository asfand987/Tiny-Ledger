package com.example.ledger.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.ledger.dto.CreateTransactionRequest;
import com.example.ledger.exception.InsufficientFundsException;
import com.example.ledger.model.Transaction;
import com.example.ledger.model.TransactionType;
import com.example.ledger.repository.InMemoryLedgerRepository;

@Service
public class LedgerService {

	private final InMemoryLedgerRepository repository;

	public LedgerService(InMemoryLedgerRepository repository) {
		this.repository = repository;
	}

	public Transaction recordTransaction(CreateTransactionRequest request) {
		if (request.type() == TransactionType.WITHDRAWAL
				&& getBalance().compareTo(request.amount()) < 0) {
			throw new InsufficientFundsException();
		}

		Transaction transaction = new Transaction(
				UUID.randomUUID(),
				request.type(),
				request.amount(),
				Instant.now());

		repository.add(transaction);
		return transaction;
	}

	public BigDecimal getBalance() {
		BigDecimal balance = BigDecimal.ZERO;
		for (Transaction tx : repository.findAll()) {
			if (tx.type() == TransactionType.DEPOSIT) {
				balance = balance.add(tx.amount());
			} else {
				balance = balance.subtract(tx.amount());
			}
		}
		return balance;
	}

	public List<Transaction> getTransactionHistory() {
		return repository.findAll();
	}

}
