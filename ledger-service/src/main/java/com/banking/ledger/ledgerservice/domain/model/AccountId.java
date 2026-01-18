package com.banking.ledger.ledgerservice.domain.model;

import java.util.UUID;

public record AccountId(UUID value) {
    public AccountId {
        if (value == null) throw new IllegalArgumentException("AccountId value cannot be null");
    }

    public static AccountId of(UUID value) {
        return new AccountId(value);
    }

    public static AccountId of(String value) {
        return new AccountId(UUID.fromString(value));
    }
}
