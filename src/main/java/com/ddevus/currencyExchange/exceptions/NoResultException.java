package com.ddevus.currencyExchange.exceptions;

import lombok.Getter;

public class NoResultException extends BasicApplicationException {

    public NoResultException(String errorMassage, String detailsOfError) {
        super.errorMessage = errorMassage;
        super.httpCodeStatus = 404;
        super.detailsOfError = detailsOfError;
    }

}
