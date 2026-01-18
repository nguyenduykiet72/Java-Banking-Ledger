package com.banking.ledger.ledgerservice.domain.model;

import com.banking.ledger.ledgerservice.domain.exception.DomainException;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money money) {
        checkCurrency(money);
        return new Money(this.amount.add(money.amount), this.currency);
    }

    public Money subtract(Money money) {
        checkCurrency(money);
        return new Money(this.amount.subtract(money.amount), this.currency);
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThanOrEqual(Money money) {
        checkCurrency(money);
        return this.amount.compareTo(money.amount) >= 0;
    }

    private void checkCurrency(Money money) {
        if (!this.currency.equals(money.currency)) {
            throw new DomainException("Currencies do not match");
        }
    }
}
