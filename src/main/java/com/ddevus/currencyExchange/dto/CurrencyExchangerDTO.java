package com.ddevus.currencyExchange.dto;

public class CurrencyExchangerDTO {

    private ExchangeRateDTO exchangeRateDTO;
    private float amount;
    private float convertedAmount;

    public CurrencyExchangerDTO(ExchangeRateDTO exchangeRateDTO, float amount, float convertedAmount) {
        this.exchangeRateDTO = exchangeRateDTO;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public ExchangeRateDTO getExchangeRateDTO() {
        return exchangeRateDTO;
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
                "\"baseCurrency\":" + exchangeRateDTO.getBaseCurrency() +
                ", \"targetCurrency\":" + exchangeRateDTO.getTargetCurrency() +
                ", \"rate\":" + exchangeRateDTO.getRate() +
                ", \"amount\":" + amount +
                ", \"convertedAmount\":" + convertedAmount +
                '}';
    }
}
