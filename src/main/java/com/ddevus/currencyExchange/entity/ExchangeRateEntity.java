package com.ddevus.currencyExchange.entity;

public class ExchangeRateEntity {

    private int id;
    private CurrencyEntity baseCurrency;
    private CurrencyEntity targetCurrency;
    private float rate;

    public ExchangeRateEntity(int id, CurrencyEntity baseCurrencyId, CurrencyEntity targetCurrency, float rate) {
        this.id = id;
        this.baseCurrency = baseCurrencyId;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrencyEntity getBaseCurrencyId() {
        return baseCurrency;
    }

    public void setBaseCurrencyId(CurrencyEntity baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyEntity getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyEntity targetCurrency) {
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
        return "ExchangeRateEntity{" +
                "id=" + id +
                ", baseCurrency=" + baseCurrency.toString() +
                ", targetCurrency=" + targetCurrency.toString() +
                ", rate=" + rate +
                '}';
    }
}
