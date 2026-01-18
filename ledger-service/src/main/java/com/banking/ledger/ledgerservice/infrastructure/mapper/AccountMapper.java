package com.banking.ledger.ledgerservice.infrastructure.mapper;

import com.banking.ledger.ledgerservice.domain.model.Account;
import com.banking.ledger.ledgerservice.domain.model.AccountId;
import com.banking.ledger.ledgerservice.domain.model.AccountState;
import com.banking.ledger.ledgerservice.domain.model.Money;
import org.springframework.stereotype.Component;
import com.banking.ledger.ledger_service.generated.jooq.tables.records.TAccountsRecord;

@Component
public class AccountMapper {
    public Account toDomain(TAccountsRecord record) {
        return new Account(
                AccountId.of(record.getTAccId()),
                Money.of(record.getTAccBalance(), record.getTAccCurrency()),
                AccountState.valueOf(record.getTAccState()),
                record.getTAccVersion()
        );
    }
}
