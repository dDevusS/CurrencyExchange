package com.ddevus.currencyExchange.exceptions;

public class SQLBadRequestException extends WrapperException {

    public SQLBadRequestException(String message, ErrorReason errorReason) {
        System.err.println(message);
        super.errorMessage = message;
        super.errorReason = errorReason;
    }

    public SQLBadRequestException(String message, ErrorReason errorReason, Exception exception) {
        System.err.println(message);
        super.errorMessage = message;
        super.exception = exception;
        super.errorReason = errorReason;
    }
}
