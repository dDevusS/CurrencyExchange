package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.entity.ExchangeRateEntity;

import java.util.List;

public interface ExchangeRateService {

    public ExchangeRateEntity save(ExchangeRateEntity exchangeRate);

    public ExchangeRateEntity findByBaseAndTargetCurrenciesId(int baseCurrencyId, int targetCurrencyId);

    public List<ExchangeRateEntity> findAll();

    public ExchangeRateEntity update(float rate);
}
