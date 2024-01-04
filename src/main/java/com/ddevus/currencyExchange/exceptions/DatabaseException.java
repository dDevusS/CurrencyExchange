package com.ddevus.currencyExchange.exceptions;

public class DatabaseException extends WrapperException {

    public DatabaseException(String message, ErrorReason errorReason) {
        logger.warning(message);
        super.errorMessage = message;
        super.errorReason = errorReason;
    }
}
