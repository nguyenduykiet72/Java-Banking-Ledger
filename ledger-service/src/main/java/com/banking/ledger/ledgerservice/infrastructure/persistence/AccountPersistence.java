package com.banking.ledger.ledgerservice.infrastructure.persistence;

import com.banking.ledger.ledger_service.generated.jooq.tables.TAccounts;
import com.banking.ledger.ledgerservice.domain.exception.DomainException;
import com.banking.ledger.ledgerservice.domain.model.Account;
import com.banking.ledger.ledgerservice.domain.model.AccountId;
import com.banking.ledger.ledgerservice.domain.respository.LoadAccountRepo;
import com.banking.ledger.ledgerservice.domain.respository.SaveAccountRepo;
import com.banking.ledger.ledgerservice.infrastructure.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountPersistence implements LoadAccountRepo, SaveAccountRepo {
    private final DSLContext dsl;
    private final AccountMapper accountMapper;

    @Override
    public Optional<Account> loadAccountById(AccountId accountId) {
       return dsl.selectFrom(TAccounts.T_ACCOUNTS)
               .where(TAccounts.T_ACCOUNTS.T_ACC_ID.eq(accountId.value()))
               .fetchOptional()
               .map(accountMapper::toDomain);
    }


    @Override
    public void saveAccount(Account account) {
        int rowUpdated = dsl.update(TAccounts.T_ACCOUNTS)
                .set(TAccounts.T_ACCOUNTS.T_ACC_BALANCE,account.getBalance().amount())
//                .set(TAccounts.T_ACCOUNTS.T_ACC_CURRENCY,account.getBalance().currency())
                .set(TAccounts.T_ACCOUNTS.T_ACC_STATE,account.getState().name())
                .set(TAccounts.T_ACCOUNTS.T_ACC_UPDATED_AT, OffsetDateTime.now())
                .set(TAccounts.T_ACCOUNTS.T_ACC_VERSION,account.getVersion() + 1)
                .where(TAccounts.T_ACCOUNTS.T_ACC_ID.eq(account.getId().value()))
                .and(TAccounts.T_ACCOUNTS.T_ACC_VERSION.eq(account.getVersion()))
                .execute();

        if (rowUpdated == 0) {
            throw new DomainException("Account not found");
        }
    }
}
