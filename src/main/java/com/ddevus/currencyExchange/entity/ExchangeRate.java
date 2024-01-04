package com.ddevus.currencyExchange.entity;

public class ExchangeRate {

    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private float rate;

    public ExchangeRate( Currency baseCurrency, Currency targetCurrency, float rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public ExchangeRate(int id, Currency baseCurrency, Currency targetCurrency, float rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id +
                "\",\"baseCurrency\":" + baseCurrency +
                ",\"targetCurrency\":" + targetCurrency +
                ",\"rate\":\"" + rate +
                "\"}";
    }
}
