package com.ddevus.currencyExchange.exceptions;

public class InsertFailedException extends BasicApplicationException {

    public InsertFailedException (String errorMessage) {
        super.errorMessage = errorMessage;
        super.HTTP_CODE_STATUS = 409;
    }

}
