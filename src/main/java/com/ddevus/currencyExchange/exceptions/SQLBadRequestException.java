package com.ddevus.currencyExchange.exceptions;

public class SQLBadRequestException extends WrapperException {

    public SQLBadRequestException(String message, ErrorReason errorReason) {
        System.err.println(message);
        super.errorMessage = message;
        super.errorReason = errorReason;
    }
}
