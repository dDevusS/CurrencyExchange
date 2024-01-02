package com.ddevus.currencyExchange.exceptions;

public class SqlBadRequestException extends RuntimeException {

    private String errorMessage;
    private ErrorReason errorReason;
    private Exception exception;

    public SqlBadRequestException(String message, ErrorReason errorReason) {
        System.err.println(message);
        this.errorMessage = message;
        this.errorReason = errorReason;
    }

    public SqlBadRequestException(String message, Exception exception, ErrorReason errorReason) {
        System.err.println(message);
        this.errorMessage = message;
        this.exception = exception;
        this.errorReason = errorReason;
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
            case FAILED_GET_LAST_OPERATION_ID -> {return  500;}
        }
        return STATUS_CODE_HTTP_RESPONSE;
    }

    public Exception getException() {
        return exception;
    }

    public enum ErrorReason {
        FAILED_INSERT, FAILED_GET_LAST_OPERATION_ID
    }
}
