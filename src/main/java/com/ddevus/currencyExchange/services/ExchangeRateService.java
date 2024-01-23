package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.dao.interfaces.IExchangeRateDAO;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.services.interfaces.IExchangeRateService;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateService implements IExchangeRateService {

    private static final IExchangeRateDAO EXCHANGE_RATE_DAO = ExchangeRateDAO.getINSTANCE();
    @Getter
    private static final IExchangeRateService INSTANCE = new ExchangeRateService();

    private ExchangeRateService() {
    }

    @Override
    public ExchangeRate save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {

        return EXCHANGE_RATE_DAO.save(baseCurrencyCode, targetCurrencyCode, rate);
    }

    @Override
    public ExchangeRate findByBaseAndTargetCurrenciesCodes(String baseCurrencyCode, String targetCurrencyCode) {

        return EXCHANGE_RATE_DAO.findByCodes(baseCurrencyCode, targetCurrencyCode);
    }

    @Override
    public List<ExchangeRate> findAll() {

        return EXCHANGE_RATE_DAO.findAll();
    }

    @Override
    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        ExchangeRate exchangeRate = findByBaseAndTargetCurrenciesCodes(baseCurrencyCode,
                targetCurrencyCode);

        if (exchangeRate == null) {
            return null;
        }

        EXCHANGE_RATE_DAO.update(exchangeRate.getId(), rate);
        exchangeRate.setRate(rate);

        return exchangeRate;
    }
}
