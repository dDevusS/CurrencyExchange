package com.ddevus.currencyExchange.dto;

public class ExchangeRateDTO {

    private int Id;
    private CurrencyDTO baseCurrency;
    private CurrencyDTO targetCurrency;
    private float rate;

    public ExchangeRateDTO(int id, CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, float rate) {
        Id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return Id;
    }

    public CurrencyDTO getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyDTO getTargetCurrency() {
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
