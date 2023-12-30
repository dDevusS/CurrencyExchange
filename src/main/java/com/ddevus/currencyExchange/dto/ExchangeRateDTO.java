package com.ddevus.currencyExchange.dto;

import com.ddevus.currencyExchange.entity.CurrencyEntity;

public class ExchangeRateDTO {

    private int Id;
    private CurrencyEntity baseCurrency;
    private CurrencyEntity targetCurrency;
    private float rate;

    public ExchangeRateDTO(int id, CurrencyEntity baseCurrency, CurrencyEntity targetCurrency, float rate) {
        Id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return Id;
    }

    public CurrencyEntity getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyEntity getTargetCurrency() {
        return targetCurrency;
    }

    public float getRate() {
        return rate;
    }

    public void setId(int id) {
        Id = id;
    }
}
