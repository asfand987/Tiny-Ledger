package com.example.ledger.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.ledger.model.Transaction;

@Repository
public class InMemoryLedgerRepository {

	private final List<Transaction> transactions = new ArrayList<>();

	public void add(Transaction transaction) {
		transactions.add(transaction);
	}

	public List<Transaction> findAll() {
		List<Transaction> copy = new ArrayList<>(transactions);
		copy.sort(Comparator.comparing(Transaction::createdAt).reversed());
		return copy;
	}

}
