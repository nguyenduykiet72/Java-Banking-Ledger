package com.banking.ledger.ledgerservice.application.command;

import com.banking.ledger.commonlib.common.exception.BusinessException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class PostTransactionCommand {
    private final String referenceId;
    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final BigDecimal amount;
    private final String currency;
    private final String description;

    public PostTransactionCommand(String referenceId, UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currency, String description) {
        this.referenceId = referenceId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.description = String.format("Transfer from %s to %s", fromAccountId, toAccountId);

        validate();
    }

    private void validate() {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Transfer must be positive", "LEDGER_003", 400);
        }
        if (fromAccountId.equals(toAccountId)) {
            throw new BusinessException("Cannot transfer to the same account", "LEDGER_004", 400);
        }
    }
}
