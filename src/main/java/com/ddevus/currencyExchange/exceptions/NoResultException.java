package com.ddevus.currencyExchange.exceptions;

public class NoResultException extends BasicApplicationException {

    public NoResultException(String errorMassage) {
        super.errorMessage = errorMassage;
        super.HTTP_CODE_STATUS = 404;
    }

}
