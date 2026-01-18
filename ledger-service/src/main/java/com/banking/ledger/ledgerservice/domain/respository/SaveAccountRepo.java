package com.banking.ledger.ledgerservice.domain.respository;

import com.banking.ledger.ledgerservice.domain.model.Account;

public interface SaveAccountRepo {
    void saveAccount(Account account);
}
