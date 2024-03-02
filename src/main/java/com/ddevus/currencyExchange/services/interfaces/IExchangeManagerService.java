package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.entity.ExchangeRate;

public interface IExchangeManagerService {

    public ExchangeRate getRequiredExchangeRate(String baseCurrencyCode, String targetCurrencyCode);
}
