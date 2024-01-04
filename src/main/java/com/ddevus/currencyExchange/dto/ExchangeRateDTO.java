package com.ddevus.currencyExchange.dto;

import com.ddevus.currencyExchange.entity.Currency;

public class ExchangeRateDTO {

    private int Id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private float rate;

    public ExchangeRateDTO(int id, Currency baseCurrency, Currency targetCurrency, float rate) {
        Id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return Id;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public float getRate() {
        return rate;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "\"Id\":" + Id +
                ", \"baseCurrency\":" + baseCurrency +
                ", \"targetCurrency\":" + targetCurrency +
                ", \"rate\":" + rate +
                "}";
    }
}
