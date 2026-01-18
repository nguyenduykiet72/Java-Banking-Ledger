package com.banking.ledger.ledgerservice.domain.respository;

import com.banking.ledger.ledgerservice.domain.model.Account;
import com.banking.ledger.ledgerservice.domain.model.AccountId;

import java.util.Optional;

public interface LoadAccountRepo {
    Optional<Account> loadAccountById(AccountId accountId);
}
