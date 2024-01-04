package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRateService;

import java.util.List;

public class ExchangeRate_Service implements ExchangeRateService {

    private static final com.ddevus.currencyExchange.dao.interfaces.ExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getINSTANCE();
    private static final com.ddevus.currencyExchange.dao.interfaces.CurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final ExchangeRateService INSTANCE= new ExchangeRate_Service();

    private ExchangeRate_Service() {}

    public static ExchangeRateService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        var savedExchangeRateEntity = exchangeRateDAO.save(exchangeRate);
        exchangeRate.setId(savedExchangeRateEntity.getId());

        return exchangeRate;
    }

    @Override
    public ExchangeRate findByBaseAndTargetCurrenciesCode(String baseCurrencyCode, String targetCurrencyCode) {
        Currency baseCurrency;
        Currency targetCurrency;

        try {
            baseCurrency = currencyDAO.findByCode(baseCurrencyCode);
            targetCurrency = currencyDAO.findByCode(targetCurrencyCode);
        }
        catch (WrapperException e) {
            e.setErrorMessage("There is no currency or currencies with those codes.");
            throw e;
        }

        return exchangeRateDAO.findByBaseAndTargetCurrencies(baseCurrency, targetCurrency);
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRateList = exchangeRateDAO.findAll();

        return exchangeRateList;
    }

    @Override
    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, float rate)
            throws WrapperException {
        ExchangeRate exchangeRate;

        try {
            Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode);
            Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode);
            exchangeRate = exchangeRateDAO
                    .findByBaseAndTargetCurrencies(baseCurrency,
                            targetCurrency);

            exchangeRateDAO.update(exchangeRate.getId(), rate);
        }
        catch (WrapperException e) {
            e.setErrorMessage("There is no currency pair with those codes in the database.");
            throw e;
        }

        return exchangeRate;
    }
}
