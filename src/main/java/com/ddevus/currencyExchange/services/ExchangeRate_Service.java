package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.dao.interfaces.ICurrencyDAO;
import com.ddevus.currencyExchange.dao.interfaces.IExchangeRateDAO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.interfaces.IExchangeRate_Service;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRate_Service implements IExchangeRate_Service {

    private static final IExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getINSTANCE();
    private static final ICurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final IExchangeRate_Service INSTANCE = new ExchangeRate_Service();

    private ExchangeRate_Service() {
    }

    public static IExchangeRate_Service getINSTANCE() {
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
        catch (SQLBadRequestException e) {
            throw new SQLBadRequestException("There is no currency or currencies with those codes."
                    , WrapperException.ErrorReason.FAILED_FIND_CURRENCY_IN_DB);
        }

        return exchangeRateDAO.findByBaseAndTargetCurrencies(baseCurrency, targetCurrency);
    }

    @Override
    public List<ExchangeRate> findAll() {

        return exchangeRateDAO.findAll();
    }

    @Override
    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        ExchangeRate exchangeRate;

        try {
            Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode);
            Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode);
            exchangeRate = exchangeRateDAO
                    .findByBaseAndTargetCurrencies(baseCurrency,
                            targetCurrency);

            exchangeRateDAO.update(exchangeRate.getId(), rate);
            exchangeRate.setRate(rate);
        }
        catch (SQLBadRequestException e) {
            throw new SQLBadRequestException("There is no currency pair with those codes in the database."
                    , WrapperException.ErrorReason.FAILED_FIND_CURRENCY_IN_DB);
        }

        return exchangeRate;
    }
}
