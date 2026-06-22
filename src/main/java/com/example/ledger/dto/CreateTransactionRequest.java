package com.example.ledger.dto;

import java.math.BigDecimal;

import com.example.ledger.model.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreateTransactionRequest(
	@NotNull TransactionType type,
	@NotNull @DecimalMin("0.01") BigDecimal amount
) {
}
