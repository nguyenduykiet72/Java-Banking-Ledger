package com.banking.ledger.ledgerservice.application.repository;

import com.banking.ledger.ledgerservice.application.command.PostTransactionCommand;

public interface PostTransactionUseCase {
    void postTransaction(PostTransactionCommand command);
}
