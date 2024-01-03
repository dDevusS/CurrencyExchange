package com.ddevus.currencyExchange.dao.interfaces;

import com.ddevus.currencyExchange.entity.ExchangeRateEntity;

import java.util.List;

public interface ExchangeRateDAO {

    public ExchangeRateEntity save(ExchangeRateEntity exchangeRate);

    public ExchangeRateEntity findByBaseAndTargetCurrenciesId(int baseCurrencyId, int targetCurrencyId);

    public List<ExchangeRateEntity> findAll();

    public boolean update(int id, float rate);

}
