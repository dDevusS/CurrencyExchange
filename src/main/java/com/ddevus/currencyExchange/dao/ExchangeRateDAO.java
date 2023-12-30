package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.ExchangeRateEntity;

import java.util.List;

public interface ExchangeRateDAO {

    public ExchangeRateEntity save(ExchangeRateEntity exchangeRate);

    public ExchangeRateEntity findByBaseAndTargetCurrenciesId(int baseCurrencyId, int targetCurrencyId);

    public List<ExchangeRateEntity> findAll();

}
