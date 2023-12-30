package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.ExchangeRateEntity;

import java.util.List;

public class ExchangeRateDAOImplementation implements ExchangeRateDAO {

    private final ExchangeRateDAOImplementation INSTANCE = new ExchangeRateDAOImplementation();

    private ExchangeRateDAOImplementation() {}

    public ExchangeRateDAOImplementation getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateEntity save(ExchangeRateEntity exchangeRate) {
        return null;
    }

    @Override
    public ExchangeRateEntity findByBaseAndTargetCurrenciesId(int baseCurrencyId, int targetCurrencyId) {
        return null;
    }

    @Override
    public List<ExchangeRateEntity> findAll() {
        return null;
    }
}
