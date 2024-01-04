package com.ddevus.currencyExchange.dao.interfaces;

import com.ddevus.currencyExchange.entity.ExchangeRate;

import java.util.List;

public interface ExchangeRateDAO {

    public ExchangeRate save(ExchangeRate exchangeRate);

    public ExchangeRate findByBaseAndTargetCurrenciesId(int baseCurrencyId, int targetCurrencyId);

    public List<ExchangeRate> findAll();

    public boolean update(int id, float rate);

}
