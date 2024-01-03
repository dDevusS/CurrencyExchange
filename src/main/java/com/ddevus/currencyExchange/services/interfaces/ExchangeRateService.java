package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.dto.ExchangeRateDTO;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateService {

    public ExchangeRateDTO save(ExchangeRateDTO exchangeRate) throws SQLException;

    public ExchangeRateDTO findByBaseAndTargetCurrenciesCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException;

    public List<ExchangeRateDTO> findAll() throws SQLException;

    public ExchangeRateDTO update(String baseCurrencyCode, String targetCurrencyCode, float rate) throws SQLException;
}
