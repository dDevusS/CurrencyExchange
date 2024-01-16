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
    public ExchangeRate save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {

        return exchangeRateDAO.save(baseCurrencyCode, targetCurrencyCode, rate);
    }

    @Override
    public ExchangeRate findByBaseAndTargetCurrenciesCodes(String baseCurrencyCode, String targetCurrencyCode) {

            return exchangeRateDAO.findByBaseAndTargetCurrenciesCodes(baseCurrencyCode, targetCurrencyCode);
    }

    @Override
    public List<ExchangeRate> findAll() {

        return exchangeRateDAO.findAll();
    }

    @Override
    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        ExchangeRate exchangeRate = findByBaseAndTargetCurrenciesCodes(baseCurrencyCode,
                            targetCurrencyCode);

            if (exchangeRate == null) {
                return null;
            }

            exchangeRateDAO.update(exchangeRate.getId(), rate);
            exchangeRate.setRate(rate);

        return exchangeRate;
    }
}
