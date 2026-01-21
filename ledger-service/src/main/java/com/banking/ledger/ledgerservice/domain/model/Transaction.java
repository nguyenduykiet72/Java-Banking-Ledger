package com.banking.ledger.ledgerservice.domain.model;

import com.banking.ledger.commonlib.common.exception.BusinessException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Transaction {
    private final TransactionId transactionId;
    private final String referenceId;
    private final String description;
    private final LocalDateTime bookingDate;
    private final List<LedgerEntry> ledgerEntries;

    public Transaction(TransactionId transactionId, String referenceId, String description, LocalDateTime bookingDate, List<LedgerEntry> ledgerEntries) {
        this.transactionId = transactionId;
        this.referenceId = referenceId;
        this.description = description;
        this.bookingDate = bookingDate;
        this.ledgerEntries = ledgerEntries;
    }

    public static Transaction create(String referenceId, String description, LocalDateTime bookingDate, List<LedgerEntry> ledgerEntries) {
        validateBalance(ledgerEntries);
        validateCurrency(ledgerEntries);
        return new Transaction(TransactionId.generate(), referenceId, description, bookingDate, ledgerEntries);
    }

    private static void validateBalance(List<LedgerEntry> ledgerEntries) {
        BigDecimal totalDebit = ledgerEntries.stream()
                .filter(e -> e.getType() == EntryType.DEBIT)
                .map(e -> e.getAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredit = ledgerEntries.stream()
                .filter(e -> e.getType() == EntryType.CREDIT)
                .map(e -> e.getAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebit.compareTo(totalCredit) > 0) {
            throw new BusinessException(
                    String.format("Transaction imbalance! Debit: %s, Credit: %s", totalDebit, totalCredit),
                    "LEDGER_IMBALANCE", 400
            );
        }
    }

    private static void validateCurrency(List<LedgerEntry> ledgerEntries) {
        if (ledgerEntries.isEmpty()) return;
        String currency = ledgerEntries.get(0).getAmount().currency();
        boolean allSameCurrency = ledgerEntries.stream()
                .allMatch(e -> e.getAmount().currency().equals(currency));

        if (!allSameCurrency) {
            throw new BusinessException(
                    "All ledger entries must have the same currency",
                    "CURRENCY_MISMATCH", 400
            );
        }
    }
}
