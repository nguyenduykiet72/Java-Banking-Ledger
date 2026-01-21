package com.banking.ledger.ledgerservice.application.service;

import com.banking.ledger.ledgerservice.application.command.PostTransactionCommand;
import com.banking.ledger.ledgerservice.application.repository.DepositUseCase;
import com.banking.ledger.ledgerservice.application.repository.PostTransactionUseCase;
import com.banking.ledger.ledgerservice.domain.exception.DomainException;
import com.banking.ledger.ledgerservice.domain.model.*;
import com.banking.ledger.ledgerservice.domain.respository.LoadAccountRepo;
import com.banking.ledger.ledgerservice.domain.respository.SaveAccountRepo;
import com.banking.ledger.ledgerservice.domain.respository.SaveTransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerAppService implements DepositUseCase, PostTransactionUseCase {
    private final LoadAccountRepo loadAccountRepo;
    private final SaveAccountRepo saveAccountRepo;
    private final SaveTransactionRepo saveTransactionRepo;

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

    @Override
    @Transactional
    public void postTransaction(PostTransactionCommand command) {
        log.info("Processing transaction: {}", command.getReferenceId());

        AccountId accountId = AccountId.of(command.getFromAccountId());
        AccountId desId = AccountId.of(command.getToAccountId());
        Money amount = Money.of(command.getAmount(), command.getCurrency());

        LedgerEntry debitEntry = LedgerEntry.debit(accountId, amount);
        LedgerEntry creditEntry = LedgerEntry.credit(desId, amount);

        Transaction transaction = Transaction.create(
                command.getReferenceId(),
                command.getDescription(),
                LocalDateTime.now(),
                List.of(debitEntry,creditEntry)
        );

        saveTransactionRepo.saveTransaction(transaction);
        log.info("Saved transaction successfully: {}", transaction);
    }
}
