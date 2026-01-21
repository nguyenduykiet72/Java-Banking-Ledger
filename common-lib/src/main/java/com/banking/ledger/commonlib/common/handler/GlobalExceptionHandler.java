package com.banking.ledger.commonlib.common.handler;

import com.banking.ledger.commonlib.common.dto.ErrorResponse;
import com.banking.ledger.commonlib.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest req) {
        log.warn("Business Error: {}",ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .statusCode(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    public ResponseEntity<ErrorResponse> handleUncaughtException(Exception ex, HttpServletRequest req) {
        log.error("Uncaught Exception: {}",ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message("An unexpected error occurred")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
