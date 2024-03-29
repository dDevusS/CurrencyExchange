package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public interface IExchangeRateService {

    public ExchangeRate save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);

    public ExchangeRate findByBaseAndTargetCurrenciesCodes(String baseCurrencyCode, String targetCurrencyCode);

    public List<ExchangeRate> findAll();

    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);
}
