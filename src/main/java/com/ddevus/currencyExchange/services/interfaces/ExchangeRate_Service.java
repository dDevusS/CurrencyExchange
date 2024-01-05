package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.entity.ExchangeRate;

import java.util.List;

public interface ExchangeRate_Service {

    public ExchangeRate save(ExchangeRate exchangeRate);

    public ExchangeRate findByBaseAndTargetCurrenciesCode(String baseCurrencyCode, String targetCurrencyCode);

    public List<ExchangeRate> findAll();

    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, float rate);
}