package com.ddevus.currencyExchange.exceptions;

public class SQLBadRequestException extends WrapperException {

    public SQLBadRequestException(String message, ErrorReason errorReason) {
        logger.info(message);
        super.errorMessage = message;
        super.errorReason = errorReason;
    }
}
