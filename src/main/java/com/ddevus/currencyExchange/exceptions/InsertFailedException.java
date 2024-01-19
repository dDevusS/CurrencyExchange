package com.ddevus.currencyExchange.exceptions;

import lombok.Getter;

public class InsertFailedException extends BasicApplicationException {

    public InsertFailedException(String errorMassage, String detailsOfError) {
        super.errorMessage = errorMassage;
        super.httpCodeStatus = 409;
        super.detailsOfError = detailsOfError;
    }

}
