package com.ddevus.currencyExchange.entity;

import java.math.BigDecimal;

public class ExchangeRateEntity {

    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private float rate;

    public ExchangeRateEntity(int id, int baseCurrencyId, int targetCurrencyId, float rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
