package com.ddevus.currencyExchange.dto;

import com.ddevus.currencyExchange.entity.ExchangeRate;

import java.math.BigDecimal;

public class ExchangeDTO {

    private ExchangeRate exchangeRate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

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
