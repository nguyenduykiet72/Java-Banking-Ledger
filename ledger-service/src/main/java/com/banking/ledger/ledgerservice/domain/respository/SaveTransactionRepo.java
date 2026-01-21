package com.banking.ledger.ledgerservice.domain.respository;


import com.banking.ledger.ledgerservice.domain.model.Transaction;

public interface SaveTransactionRepo {
    void saveTransaction(Transaction transaction);
}
