package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dao.CurrencyDAOImplementation;
import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.dao.ExchangeRateDAOImplementation;
import com.ddevus.currencyExchange.dto.ExchangeRateDTO;
import com.ddevus.currencyExchange.entity.ExchangeRateEntity;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRateServiceImplementation implements ExchangeRateService {

    private static final ExchangeRateDAO exchangeRateDAO = ExchangeRateDAOImplementation.getINSTANCE();
    private static final CurrencyDAO currencyDAO = CurrencyDAOImplementation.getINSTANCE();
    private static final ExchangeRateService INSTANCE= new ExchangeRateServiceImplementation();

    private ExchangeRateServiceImplementation() {}

    public static ExchangeRateService getINSTANCE() {
        return INSTANCE;
    }

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
    public ExchangeRateDTO findByBaseAndTargetCurrenciesCode(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            var baseCurrency = currencyDAO.findByCode(baseCurrencyCode).get();
            var targetCurrency = currencyDAO.findByCode(targetCurrencyCode).get();
            var exchangeRate = exchangeRateDAO
                    .findByBaseAndTargetCurrenciesId(baseCurrency.getId(),
                            targetCurrency.getId());

            ExchangeRateDTO exchangeRateDTO
                    = new ExchangeRateDTO(exchangeRate.getId(), baseCurrency, targetCurrency, exchangeRate.getRate());
            return exchangeRateDTO;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ExchangeRateDTO> findAll() {
        List<ExchangeRateEntity> exchangeRateEntityList = exchangeRateDAO.findAll();

        List<ExchangeRateDTO> exchangeRateDTOList = new ArrayList<>();
        for (ExchangeRateEntity entity : exchangeRateEntityList) {
            var baseCurrency = currencyDAO.findById(entity.getBaseCurrencyId()).get();
            var targetCurrency = currencyDAO.findById(entity.getTargetCurrencyId()).get();

            ExchangeRateDTO exchangeRateDTO
                    = new ExchangeRateDTO(entity.getId(), baseCurrency, targetCurrency, entity.getRate());

            exchangeRateDTOList.add(exchangeRateDTO);
        }

        return exchangeRateDTOList;
    }

    @Override
    public ExchangeRateDTO update(String baseCurrencyCode, String targetCurrencyCode, float rate) {
        try {
            var baseCurrency = currencyDAO.findByCode(baseCurrencyCode).get();
            var targetCurrency = currencyDAO.findByCode(targetCurrencyCode).get();
            var exchangeRate = exchangeRateDAO
                    .findByBaseAndTargetCurrenciesId(baseCurrency.getId(),
                            targetCurrency.getId());

            exchangeRateDAO.update(exchangeRate.getId(), rate);

                ExchangeRateDTO exchangeRateDTO
                        = new ExchangeRateDTO(exchangeRate.getId(), baseCurrency, targetCurrency, exchangeRate.getRate());
                return exchangeRateDTO;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
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
