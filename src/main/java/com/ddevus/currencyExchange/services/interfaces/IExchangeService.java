package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.dto.ExchangeDTO;

import java.math.BigDecimal;

public interface IExchangeService {

    public ExchangeDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount);
}
