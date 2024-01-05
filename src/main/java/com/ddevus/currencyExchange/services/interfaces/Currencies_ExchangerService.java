package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.dto.ExchangeDTO;

public interface Currencies_ExchangerService {

    public ExchangeDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, float amount);
}
