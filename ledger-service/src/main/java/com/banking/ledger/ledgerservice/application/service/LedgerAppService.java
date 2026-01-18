package com.banking.ledger.ledgerservice.application.service;

import com.banking.ledger.ledgerservice.application.repository.DepositUseCase;
import com.banking.ledger.ledgerservice.domain.exception.DomainException;
import com.banking.ledger.ledgerservice.domain.model.Account;
import com.banking.ledger.ledgerservice.domain.model.AccountId;
import com.banking.ledger.ledgerservice.domain.model.Money;
import com.banking.ledger.ledgerservice.domain.respository.LoadAccountRepo;
import com.banking.ledger.ledgerservice.domain.respository.SaveAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerAppService implements DepositUseCase {
    private final LoadAccountRepo loadAccountRepo;
    private final SaveAccountRepo saveAccountRepo;

    @Override
    @Transactional
    public void deposit(UUID accountId, BigDecimal amount, String currency) {
        AccountId accId = AccountId.of(accountId);

        Account account = loadAccountRepo.loadAccountById(accId)
                .orElseThrow(() -> new DomainException("Account not found"));

        Money depositAmount = Money.of(amount, currency);
        account.deposit(depositAmount);

        saveAccountRepo.saveAccount(account);
    }
}
