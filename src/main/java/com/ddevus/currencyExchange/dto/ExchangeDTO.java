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

    @Override
    public String toString() {
        return "{" +
                "\"baseCurrency\":" + exchangeRate.getBaseCurrency() +
                ",\"targetCurrency\":" + exchangeRate.getTargetCurrency() +
                ",\"rate\":\"" + exchangeRate.getRate() +
                "\",\"amount\":\"" + amount +
                "\",\"convertedAmount\":\"" + convertedAmount +
                "\"}";
    }
}
