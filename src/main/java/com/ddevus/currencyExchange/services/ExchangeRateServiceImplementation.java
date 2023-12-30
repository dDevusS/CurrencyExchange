package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dao.CurrencyDAOImplementation;
import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.dao.ExchangeRateDAOImplementation;
import com.ddevus.currencyExchange.dto.ExchangeRateDTO;
import com.ddevus.currencyExchange.entity.ExchangeRateEntity;

import java.util.List;

public class ExchangeRateServiceImplementation implements ExchangeRateService {

    private final ExchangeRateDAO exchangeRateDAO = ExchangeRateDAOImplementation.getINSTANCE();
    private final CurrencyDAO currencyDAO = CurrencyDAOImplementation.getINSTANCE();

    @Override
    public ExchangeRateDTO save(ExchangeRateDTO exchangeRateDTO) {
        var exchangeRateEntity = convertExchangeRateDtoToEntity(exchangeRateDTO);

        try {
            var savedExchangeRateEntity = exchangeRateDAO.save(exchangeRateEntity);
            exchangeRateDTO.setId(savedExchangeRateEntity.getId());

            return exchangeRateDTO;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRateDTO findByBaseAndTargetCurrenciesId(String baseCurrencyCode, String targetCurrencyCode) {
        var baseCurrency = currencyDAO.findByCode(baseCurrencyCode).get();
        var targetCurrency = currencyDAO.findByCode(targetCurrencyCode).get();
        var exchangeRate = exchangeRateDAO
                .findByBaseAndTargetCurrenciesId(baseCurrency.getId(),
                        targetCurrency.getId());
        return null;
    }

    @Override
    public List<ExchangeRateDTO> findAll() {
        return null;
    }

    @Override
    public ExchangeRateDTO update(float rate) {
        return null;
    }

    private static ExchangeRateEntity convertExchangeRateDtoToEntity(ExchangeRateDTO exchangeRateDTO) {
        ExchangeRateEntity exchangeRateEntity
                = new ExchangeRateEntity(exchangeRateDTO.getId(),
                exchangeRateDTO.getBaseCurrency().getId(),
                exchangeRateDTO.getTargetCurrency().getId(),
                exchangeRateDTO.getRate());

        return exchangeRateEntity;
    }
}
