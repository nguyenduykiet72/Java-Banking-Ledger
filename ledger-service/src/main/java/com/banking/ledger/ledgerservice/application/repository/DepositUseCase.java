package com.banking.ledger.ledgerservice.application.repository;

import java.math.BigDecimal;
import java.util.UUID;

public interface DepositUseCase {
    void deposit(UUID accountId, BigDecimal amount, String currency);
}
