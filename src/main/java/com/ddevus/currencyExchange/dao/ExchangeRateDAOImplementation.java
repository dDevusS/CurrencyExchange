package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.ExchangeRateEntity;

import java.util.List;

public class ExchangeRateDAOImplementation implements ExchangeRateDAO {

    private ExchangeRateDAOImplementation exchangeRateDAO = new ExchangeRateDAOImplementation();

    private ExchangeRateDAOImplementation() {}

    public ExchangeRateDAOImplementation getExchangeRateDAO() {
        return exchangeRateDAO;
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
