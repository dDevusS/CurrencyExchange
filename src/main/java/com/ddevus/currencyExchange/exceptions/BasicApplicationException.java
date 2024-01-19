package com.ddevus.currencyExchange.exceptions;

import lombok.Getter;

@Getter
public abstract class BasicApplicationException extends RuntimeException {

    protected String errorMessage;
    protected int httpCodeStatus;

}
