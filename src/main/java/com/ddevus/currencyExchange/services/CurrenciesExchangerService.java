package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dto.CurrencyExchangerDTO;

public interface CurrenciesExchangerService {

    public CurrencyExchangerDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, float amount);
}
