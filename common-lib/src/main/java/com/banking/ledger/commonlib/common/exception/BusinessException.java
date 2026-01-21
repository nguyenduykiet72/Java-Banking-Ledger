package com.banking.ledger.commonlib.common.exception;

public class BusinessException extends BaseException {
    public BusinessException(String message, String errorCode, int statusCode) {
        super(message, errorCode, statusCode);
    }
}
