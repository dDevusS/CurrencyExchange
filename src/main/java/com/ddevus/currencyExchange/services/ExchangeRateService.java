package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dto.ExchangeRateDTO;

import java.util.List;

public interface ExchangeRateService {

    public ExchangeRateDTO save(ExchangeRateDTO exchangeRate);

    public ExchangeRateDTO findByBaseAndTargetCurrenciesId(String baseCurrencyCode, String targetCurrencyCode);

    public List<ExchangeRateDTO> findAll();

    public ExchangeRateDTO update(float rate);
}