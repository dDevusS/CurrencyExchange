package com.ddevus.currencyExchange.exceptions;

public abstract class WrapperException extends RuntimeException {
    protected String errorMessage;
    protected ErrorReason errorReason;
    protected Exception exception;

    public enum ErrorReason {
        FAILED_INSERT, FAILED_GET_LAST_OPERATION_ID, FAILED_READ_PROPERTIES, FAILED_FIND_JDBC_DRIVER,
        UNKNOWN_ERROR_CONNECTING_TO_DB
    }

    @Override
    public String toString() {
        return "{\"errorMessage\":\"" + errorMessage + "\"}";
    }

    public Exception getException() {
        return exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getSTATUS_CODE_HTTP_RESPONSE() {
        int STATUS_CODE_HTTP_RESPONSE = 500;
        switch (errorReason) {
            case FAILED_INSERT -> {return 409;}
            case FAILED_GET_LAST_OPERATION_ID, FAILED_READ_PROPERTIES,
                    FAILED_FIND_JDBC_DRIVER, UNKNOWN_ERROR_CONNECTING_TO_DB -> {return  500;}
        }
        return STATUS_CODE_HTTP_RESPONSE;
    }

}
