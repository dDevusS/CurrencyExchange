package com.ddevus.currencyExchange.exceptions;

public class IncorrectParametersException extends WrapperException {

    public IncorrectParametersException(String errorMassage, ErrorReason errorReason) {
        super.errorMessage = errorMassage;
        super.errorReason = errorReason;
    }
}
