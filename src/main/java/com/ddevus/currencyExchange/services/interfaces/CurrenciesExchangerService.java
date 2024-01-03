package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.dto.CurrencyExchangerDTO;

public interface CurrenciesExchangerService {

    public CurrencyExchangerDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, float amount);
}
