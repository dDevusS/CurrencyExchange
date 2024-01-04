package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.dto.ExchangeRateDTO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;

public class DtoEntityConvertor {

    public static Currency convertCurrencyDtoToEntity(CurrencyDTO currencyDTO) {
        return new Currency(currencyDTO.getId()
                , currencyDTO.getCode()
                , currencyDTO.getName()
                , currencyDTO.getSing());
    }

    public static CurrencyDTO convertCurrencyEntityToDto(Currency currency) {
        return new CurrencyDTO(currency.getId()
                , currency.getName()
                , currency.getCode()
                , currency.getSing());
    }

    public static ExchangeRate convertExchangeRateDtoToEntity(ExchangeRateDTO exchangeRateDTO) {
        ExchangeRate exchangeRate
                = new ExchangeRate(exchangeRateDTO.getId()
                , exchangeRateDTO.getBaseCurrency().getId()
                , exchangeRateDTO.getTargetCurrency().getId()
                , exchangeRateDTO.getRate());

        return exchangeRate;
    }
}
