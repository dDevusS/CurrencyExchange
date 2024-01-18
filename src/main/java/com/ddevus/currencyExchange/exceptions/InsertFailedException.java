package com.ddevus.currencyExchange.exceptions;

public class InsertFailedException extends BasicApplicationException {

    public InsertFailedException(String errorMassage) {
        super.errorMessage = errorMassage;
        super.HTTP_CODE_STATUS = 409;
    }

}
