package com.banking.ledger.ledgerservice.controller;

import com.banking.ledger.ledgerservice.application.repository.DepositUseCase;
import com.banking.ledger.ledgerservice.controller.dto.DepositRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ledgers")
@RequiredArgsConstructor
@Validated
public class LedgerController {
    private final DepositUseCase depositUseCase;

    @PostMapping
    public ResponseEntity<Void> deposit(
            @PathVariable @NotNull(message = "id is not valid") UUID accountId,
            @RequestBody @Valid DepositRequest request
            ) {
        depositUseCase.deposit(accountId,request.getAmount(), request.getCurrency());
        return ResponseEntity.ok().build();
    }
}
