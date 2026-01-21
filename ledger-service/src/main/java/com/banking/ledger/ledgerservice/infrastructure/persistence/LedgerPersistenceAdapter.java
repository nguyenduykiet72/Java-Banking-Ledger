package com.banking.ledger.ledgerservice.infrastructure.persistence;

import com.banking.ledger.commonlib.common.exception.BusinessException;
import com.banking.ledger.ledgerservice.domain.model.EntryType;
import com.banking.ledger.ledgerservice.domain.model.LedgerEntry;
import com.banking.ledger.ledgerservice.domain.model.Transaction;
import com.banking.ledger.ledgerservice.domain.respository.SaveTransactionRepo;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static com.banking.ledger.ledger_service.generated.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class LedgerPersistenceAdapter implements SaveTransactionRepo {
    private final DSLContext dslContext;


    @Override
    @Transactional
    public void saveTransaction(Transaction transaction) {
        try {
            dslContext.insertInto(T_TRANSACTIONS)
                    .set(T_TRANSACTIONS.T_TRANS_ID, transaction.getTransactionId().value())
                    .set(T_TRANSACTIONS.T_TRANS_REFERENCE_ID, transaction.getReferenceId())
                    .set(T_TRANSACTIONS.T_TRANS_BOOKING_DATE, OffsetDateTime.now())
                    .set(T_TRANSACTIONS.T_TRANS_DESCRIPTION, transaction.getDescription())
                    .execute();

            for (LedgerEntry entry : transaction.getLedgerEntries()) {
                dslContext.insertInto(T_LEDGER_ENTRIES)
                        .set(T_LEDGER_ENTRIES.T_LED_ID, UUID.randomUUID())
                        .set(T_LEDGER_ENTRIES.T_LED_TRANSACTION_ID, transaction.getTransactionId().value())
                        .set(T_LEDGER_ENTRIES.T_LED_ACCOUNT_ID, entry.getAccountId().value())
                        .set(T_LEDGER_ENTRIES.T_LED_TYPE, entry.getType().name()) // DEBIT/CREDIT
                        .set(T_LEDGER_ENTRIES.T_LED_AMOUNT, entry.getAmount().amount())
                        .execute();
                updateAccountBalance(entry);
            }        
        } catch (DuplicateKeyException e) {
            throw new BusinessException("Transaction already processed", "LEDGER_001", 409);
        }
    }

    private void updateAccountBalance(LedgerEntry entry) {
        BigDecimal amountChange = entry.getType() == EntryType.CREDIT
                ? entry.getAmount().amount()
                : entry.getAmount().amount().negate(); // For DEBIT, subtract the amount

        int updatedRows = dslContext.update(T_ACCOUNTS)
                .set(T_ACCOUNTS.T_ACC_BALANCE, T_ACCOUNTS.T_ACC_BALANCE.add(amountChange))
                .set(T_ACCOUNTS.T_ACC_VERSION, T_ACCOUNTS.T_ACC_VERSION.add(1L))
                .where(T_ACCOUNTS.T_ACC_ID.eq(entry.getAccountId().value()))
                .execute();

        if (updatedRows == 0) {
            throw new BusinessException("Account not found for ledger entry", "LEDGER_002", 404);
        }
    }
}
