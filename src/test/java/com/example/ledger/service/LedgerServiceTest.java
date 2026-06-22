package com.example.ledger.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.ledger.dto.CreateTransactionRequest;
import com.example.ledger.exception.InsufficientFundsException;
import com.example.ledger.model.Transaction;
import com.example.ledger.model.TransactionType;
import com.example.ledger.repository.InMemoryLedgerRepository;

class LedgerServiceTest {

	private LedgerService ledgerService;
	private InMemoryLedgerRepository repository;

	@BeforeEach
	void setUp() {
		repository = new InMemoryLedgerRepository();
		ledgerService = new LedgerService(repository);
	}

	@Test
	void depositIncreasesBalance() {
		ledgerService.recordTransaction(new CreateTransactionRequest(TransactionType.DEPOSIT, new BigDecimal("100.00")));

		assertEquals(new BigDecimal("100.00"), ledgerService.getBalance());
	}

	@Test
	void withdrawalDecreasesBalance() {
		ledgerService.recordTransaction(new CreateTransactionRequest(TransactionType.DEPOSIT, new BigDecimal("100.00")));
		ledgerService.recordTransaction(new CreateTransactionRequest(TransactionType.WITHDRAWAL, new BigDecimal("40.00")));

		assertEquals(new BigDecimal("60.00"), ledgerService.getBalance());
	}

	@Test
	void multipleTransactionsComputeCorrectBalance() {
		ledgerService.recordTransaction(new CreateTransactionRequest(TransactionType.DEPOSIT, new BigDecimal("100.00")));
		ledgerService.recordTransaction(new CreateTransactionRequest(TransactionType.DEPOSIT, new BigDecimal("50.00")));
		ledgerService.recordTransaction(new CreateTransactionRequest(TransactionType.WITHDRAWAL, new BigDecimal("30.00")));

		assertEquals(new BigDecimal("120.00"), ledgerService.getBalance());
	}

	@Test
	void withdrawalOverBalanceThrowsInsufficientFunds() {
		ledgerService.recordTransaction(new CreateTransactionRequest(TransactionType.DEPOSIT, new BigDecimal("50.00")));

		assertThrows(InsufficientFundsException.class, () ->
				ledgerService.recordTransaction(new CreateTransactionRequest(TransactionType.WITHDRAWAL, new BigDecimal("100.00"))));

		assertEquals(new BigDecimal("50.00"), ledgerService.getBalance());
	}

	@Test
	void historyReturnsNewestFirst() {
		Transaction older = new Transaction(UUID.randomUUID(), TransactionType.DEPOSIT, new BigDecimal("10.00"),
				Instant.parse("2026-01-01T10:00:00Z"));
		Transaction newer = new Transaction(UUID.randomUUID(), TransactionType.DEPOSIT, new BigDecimal("20.00"),
				Instant.parse("2026-01-02T10:00:00Z"));

		repository.add(older);
		repository.add(newer);

		assertEquals(newer, ledgerService.getTransactionHistory().get(0));
		assertEquals(older, ledgerService.getTransactionHistory().get(1));
	}

}
