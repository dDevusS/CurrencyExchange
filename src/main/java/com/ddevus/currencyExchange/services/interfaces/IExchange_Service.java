package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.dto.ExchangeDTO;

import java.math.BigDecimal;

public interface IExchange_Service {

    public ExchangeDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount);
}
