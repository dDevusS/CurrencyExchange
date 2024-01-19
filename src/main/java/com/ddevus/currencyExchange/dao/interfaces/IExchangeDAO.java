package com.ddevus.currencyExchange.dao.interfaces;

import com.ddevus.currencyExchange.entity.ExchangeRate;

public interface IExchangeDAO {

    public ExchangeRate getRequiredExchangeRate(String baseCurrencyCode, String targetCurrencyCode);
}
