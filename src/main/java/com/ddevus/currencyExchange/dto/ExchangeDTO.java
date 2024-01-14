package com.ddevus.currencyExchange.dto;

import com.ddevus.currencyExchange.entity.ExchangeRate;

import java.math.BigDecimal;

public class ExchangeDTO {

    private final ExchangeRate exchangeRate;
    private final BigDecimal amount;
    private final BigDecimal convertedAmount;

    public ExchangeDTO(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal convertedAmount) {
        this.exchangeRate = exchangeRate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

}
