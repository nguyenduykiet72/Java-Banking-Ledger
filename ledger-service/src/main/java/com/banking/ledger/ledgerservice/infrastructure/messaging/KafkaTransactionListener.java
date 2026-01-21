package com.banking.ledger.ledgerservice.infrastructure.messaging;

import com.banking.ledger.ledgerservice.application.command.PostTransactionCommand;
import com.banking.ledger.ledgerservice.application.repository.PostTransactionUseCase;
import com.banking.ledger.ledgerservice.infrastructure.messaging.event.TransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTransactionListener {
    private final PostTransactionUseCase postTransactionUseCase;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "fund-transfer-initiated", groupId = "ledger-service-group")
    public void onFundTransferInitiated(String message) {
        try {
            TransferEvent event = objectMapper.readValue(message, TransferEvent.class);
            log.info("Receive transfer request: {}", event.getReferenceId());

            PostTransactionCommand command = new PostTransactionCommand(
                    event.getReferenceId(),
                    event.getFromAccountId(),
                    event.getToAccountId(),
                    event.getAmount(),
                    event.getCurrency(),
                    event.getDescription()
            );

            postTransactionUseCase.postTransaction(command);

            kafkaTemplate.send("ledger-posting-completed", String.format("{\"refId\":\"%s\", \"status\":\"SUCCESS\"}", event.getReferenceId()));
        } catch (Exception e) {
            log.error("Error while posting transaction", e);
            kafkaTemplate.send("ledger-posting-failed", String.format("{\"error\":\"%s\"}", e.getMessage()));
        }
    }
}
