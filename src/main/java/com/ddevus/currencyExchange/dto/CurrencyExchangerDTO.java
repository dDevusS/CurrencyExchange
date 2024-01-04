package com.ddevus.currencyExchange.dto;

import com.ddevus.currencyExchange.entity.ExchangeRate;

public class CurrencyExchangerDTO {

    private ExchangeRate exchangeRate;
    private float amount;
    private float convertedAmount;

    public CurrencyExchangerDTO(ExchangeRate exchangeRate, float amount, float convertedAmount) {
        this.exchangeRate = exchangeRate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public ExchangeRate getExchangeRateDTO() {
        return exchangeRate;
    }

    public float getAmount() {
        return amount;
    }

    public float getConvertedAmount() {
        return convertedAmount;
    }

    @Override
    public String toString() {
        return "{" +
                "\"baseCurrency\":" + exchangeRate.getBaseCurrency() +
                ", \"targetCurrency\":" + exchangeRate.getTargetCurrency() +
                ", \"rate\":" + exchangeRate.getRate() +
                ", \"amount\":" + amount +
                ", \"convertedAmount\":" + convertedAmount +
                '}';
    }
}
