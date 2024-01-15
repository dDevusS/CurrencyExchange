package com.ddevus.currencyExchange.dao.interfaces;

import com.ddevus.currencyExchange.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public interface IExchangeRateDAO {

    public ExchangeRate save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);

    public ExchangeRate findByBaseAndTargetCurrenciesCodes(String baseCurrencyCode, String targetCurrencyCode);

    public List<ExchangeRate> findAll();

    public boolean update(int id, BigDecimal rate);

    public ExchangeRate getRequiredExchangeRate(String baseCurrencyCode, String targetCurrencyCode);

}
