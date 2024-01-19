package com.ddevus.currencyExchange.exceptions;

public class InsertFailedException extends BasicApplicationException {

    public InsertFailedException(String errorMassage) {
        super.errorMessage = errorMassage;
        super.httpCodeStatus = 409;
    }

}
