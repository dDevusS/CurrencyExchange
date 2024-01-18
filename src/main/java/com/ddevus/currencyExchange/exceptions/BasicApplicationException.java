package com.ddevus.currencyExchange.exceptions;

public abstract class BasicApplicationException extends RuntimeException {

    protected String errorMessage;
    protected int HTTP_CODE_STATUS;

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getHTTP_CODE_STATUS() {
        return HTTP_CODE_STATUS;
    }
}
