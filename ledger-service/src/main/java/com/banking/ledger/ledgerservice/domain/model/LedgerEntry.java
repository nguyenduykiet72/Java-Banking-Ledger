package com.banking.ledger.ledgerservice.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LedgerEntry {
    private final AccountId accountId;
    private final Money amount;
    private final EntryType type;

    public static LedgerEntry debit(AccountId accountId, Money amount) {
        return new LedgerEntry(accountId, amount, EntryType.DEBIT);
    }

    public static LedgerEntry credit(AccountId accountId, Money amount) {
        return new LedgerEntry(accountId, amount, EntryType.CREDIT);
    }
}
