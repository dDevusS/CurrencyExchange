package com.ddevus.currencyExchange.exceptions;

public class DatabaseException extends BasicApplicationException {

    public DatabaseException(String errorMassage) {
        super.errorMessage = errorMassage;
        super.httpCodeStatus = 500;
        super.detailsOfError = "";
    }
}
