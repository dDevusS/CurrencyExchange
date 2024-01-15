package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.dao.interfaces.ICurrencyDAO;
import com.ddevus.currencyExchange.dao.interfaces.IExchangeRateDAO;
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
    public ExchangeRate findByBaseAndTargetCurrenciesCodes(String baseCurrencyCode, String targetCurrencyCode) {

        try {

            return exchangeRateDAO.findByBaseAndTargetCurrenciesCodes(baseCurrencyCode, targetCurrencyCode);
        }
        catch (SQLBadRequestException e) {
            throw new SQLBadRequestException("There is no currency or currencies with those codes."
                    , WrapperException.ErrorReason.FAILED_FIND_CURRENCY_IN_DB);
        }
    }

    @Override
    public List<ExchangeRate> findAll() {

        return exchangeRateDAO.findAll();
    }

    @Override
    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        ExchangeRate exchangeRate;

            exchangeRate = findByBaseAndTargetCurrenciesCodes(baseCurrencyCode,
                            targetCurrencyCode);

            if (!exchangeRateDAO.update(exchangeRate.getId(), rate)) {
                return null;
            }
            exchangeRate.setRate(rate);

        return exchangeRate;
    }
}
