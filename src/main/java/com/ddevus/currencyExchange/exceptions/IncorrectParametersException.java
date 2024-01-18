package com.ddevus.currencyExchange.exceptions;

public class IncorrectParametersException extends BasicApplicationException {

    public IncorrectParametersException(String errorMassage) {
        super.errorMessage = errorMassage;
        super.HTTP_CODE_STATUS = 400;
    }

}
