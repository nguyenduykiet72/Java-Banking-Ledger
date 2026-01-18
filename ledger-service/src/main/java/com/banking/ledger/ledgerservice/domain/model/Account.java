package com.banking.ledger.ledgerservice.domain.model;

import com.banking.ledger.ledgerservice.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Account {
    private final AccountId id;
    private Money balance;
    private AccountState state;
    private Long version;

    public static Account create(AccountId id, String currency) {
        return new Account(id, Money.zero(currency),AccountState.ACTIVE, 0L);
    }

    public void withDraw(Money amount) {
        validateState();
        if (!this.balance.isGreaterThanOrEqual(amount)) {
            throw new DomainException("Insufficient balance");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void deposit(Money amount) {
        validateState();
        this.balance = this.balance.add(amount);
    }

    private void validateState() {
        if (this.state != AccountState.ACTIVE) {
            throw new DomainException("Account is not active");
        }
    }

}
