package com.ddevus.currencyExchange.exceptions;

public class NoResultException extends BasicApplicationException {

    public NoResultException(String errorMassage) {
        super.errorMessage = errorMassage;
        super.httpCodeStatus = 404;
    }

}
