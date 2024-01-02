package com.ddevus.currencyExchange.exceptions;

public class DataBaseException extends RuntimeException {

    private String errorMessage;
    private final int STATUS_CODE_HTTP_RESPONSE = 500;
    private Exception exception;

    public DataBaseException(String message, Exception exception) {
        System.err.println(message);
        this.errorMessage = message;
        this.exception = exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getSTATUS_CODE_HTTP_RESPONSE() {
        return STATUS_CODE_HTTP_RESPONSE;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "{" +
                "\"errorMessage\":\"" + errorMessage +
                "\"}";
    }
}
