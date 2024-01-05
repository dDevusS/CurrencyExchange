package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRate_Service implements com.ddevus.currencyExchange.services.interfaces.IExchangeRate_Service {

    private static final ExchangeRateDAO I_EXCHANGE_RATE_DAO = ExchangeRateDAO.getINSTANCE();
    private static final CurrencyDAO I_CURRENCY_DAO = CurrencyDAO.getINSTANCE();
    private static final com.ddevus.currencyExchange.services.interfaces.IExchangeRate_Service INSTANCE= new ExchangeRate_Service();

    private ExchangeRate_Service() {}

    public static com.ddevus.currencyExchange.services.interfaces.IExchangeRate_Service getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        var savedExchangeRateEntity = I_EXCHANGE_RATE_DAO.save(exchangeRate);
        exchangeRate.setId(savedExchangeRateEntity.getId());

        return exchangeRate;
    }

    @Override
    public ExchangeRate findByBaseAndTargetCurrenciesCode(String baseCurrencyCode, String targetCurrencyCode) {
        Currency baseCurrency;
        Currency targetCurrency;

        try {
            baseCurrency = I_CURRENCY_DAO.findByCode(baseCurrencyCode);
            targetCurrency = I_CURRENCY_DAO.findByCode(targetCurrencyCode);
        }
        catch (SQLBadRequestException e) {
            throw new SQLBadRequestException("There is no currency or currencies with those codes."
            , WrapperException.ErrorReason.FAILED_FIND_CURRENCY_IN_DB);
        }

        return I_EXCHANGE_RATE_DAO.findByBaseAndTargetCurrencies(baseCurrency, targetCurrency);
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRateList = I_EXCHANGE_RATE_DAO.findAll();

        return exchangeRateList;
    }

    @Override
    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        ExchangeRate exchangeRate;

        try {
            Currency baseCurrency = I_CURRENCY_DAO.findByCode(baseCurrencyCode);
            Currency targetCurrency = I_CURRENCY_DAO.findByCode(targetCurrencyCode);
            exchangeRate = I_EXCHANGE_RATE_DAO
                    .findByBaseAndTargetCurrencies(baseCurrency,
                            targetCurrency);

            I_EXCHANGE_RATE_DAO.update(exchangeRate.getId(), rate);
        }
        catch (SQLBadRequestException e) {
            throw new SQLBadRequestException("There is no currency pair with those codes in the database."
            , WrapperException.ErrorReason.FAILED_FIND_CURRENCY_IN_DB);
        }

        return exchangeRate;
    }
}
