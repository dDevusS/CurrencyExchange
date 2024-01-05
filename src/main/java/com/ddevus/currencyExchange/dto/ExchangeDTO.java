package com.ddevus.currencyExchange.dto;

import com.ddevus.currencyExchange.entity.ExchangeRate;

public class ExchangeDTO {

    private ExchangeRate exchangeRate;
    private float amount;
    private float convertedAmount;

    public ExchangeDTO(ExchangeRate exchangeRate, float amount, float convertedAmount) {
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
                ",\"targetCurrency\":" + exchangeRate.getTargetCurrency() +
                ",\"rate\":\"" + exchangeRate.getRate() +
                "\",\"amount\":\"" + amount +
                "\",\"convertedAmount\":\"" + convertedAmount +
                "\"}";
    }
}
