package com.ddevus.currencyExchange.dto;

public class CurrencyExchangerDTO {

    private CurrencyDTO baseCurrencyDTO;
    private CurrencyDTO targetCurrencyDTO;
    private float rate;
    private float amount;
    private float convertedAmount;

    public CurrencyExchangerDTO(CurrencyDTO baseCurrencyDTO, CurrencyDTO targetCurrencyDTO, float rate, float amount, float convertedAmount) {
        this.baseCurrencyDTO = baseCurrencyDTO;
        this.targetCurrencyDTO = targetCurrencyDTO;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public CurrencyDTO getBaseCurrencyDTO() {
        return baseCurrencyDTO;
    }

    public CurrencyDTO getTargetCurrencyDTO() {
        return targetCurrencyDTO;
    }

    public float getRate() {
        return rate;
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
                "\"baseCurrency\":" + baseCurrencyDTO +
                ", \"targetCurrency\":" + targetCurrencyDTO +
                ", \"rate\":" + rate +
                ", \"amount\":" + amount +
                ", \"convertedAmount\":" + convertedAmount +
                '}';
    }
}
