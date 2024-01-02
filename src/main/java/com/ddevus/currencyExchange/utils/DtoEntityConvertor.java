package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.dto.CurrencyDTO;
import com.ddevus.currencyExchange.dto.ExchangeRateDTO;
import com.ddevus.currencyExchange.entity.CurrencyEntity;
import com.ddevus.currencyExchange.entity.ExchangeRateEntity;

public class DtoEntityConvertor {

    public static CurrencyEntity convertCurrencyDtoToEntity(CurrencyDTO currencyDTO) {
        return new CurrencyEntity(currencyDTO.getId()
                , currencyDTO.getCode()
                , currencyDTO.getName()
                , currencyDTO.getSing());
    }

    public static CurrencyDTO convertCurrencyEntityToDto(CurrencyEntity currencyEntity) {
        return new CurrencyDTO(currencyEntity.getId()
                , currencyEntity.getName()
                , currencyEntity.getCode()
                , currencyEntity.getSing());
    }

    public static ExchangeRateEntity convertExchangeRateDtoToEntity(ExchangeRateDTO exchangeRateDTO) {
        ExchangeRateEntity exchangeRateEntity
                = new ExchangeRateEntity(exchangeRateDTO.getId()
                , exchangeRateDTO.getBaseCurrency().getId()
                , exchangeRateDTO.getTargetCurrency().getId()
                , exchangeRateDTO.getRate());

        return exchangeRateEntity;
    }
}
