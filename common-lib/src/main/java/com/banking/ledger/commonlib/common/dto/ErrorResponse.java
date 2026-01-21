package com.banking.ledger.commonlib.common.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String statusCode;
    private String message;
    private String detail;
    private LocalDateTime timestamp;
    private String path;
}
