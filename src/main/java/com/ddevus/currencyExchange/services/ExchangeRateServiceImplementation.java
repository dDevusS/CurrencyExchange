package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.interfaces.CurrencyDAO;
import com.ddevus.currencyExchange.dao.CurrencyDAOImplementation;
import com.ddevus.currencyExchange.dao.interfaces.ExchangeRateDAO;
import com.ddevus.currencyExchange.dao.ExchangeRateDAOImplementation;
import com.ddevus.currencyExchange.dto.ExchangeRateDTO;
import com.ddevus.currencyExchange.entity.CurrencyEntity;
import com.ddevus.currencyExchange.entity.ExchangeRateEntity;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRateService;
import com.ddevus.currencyExchange.utils.DtoEntityConvertor;

import java.sql.SQLException;
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
    public ExchangeRateDTO save(ExchangeRateDTO exchangeRateDTO) throws WrapperException, SQLException {
        var exchangeRateEntity = DtoEntityConvertor.convertExchangeRateDtoToEntity(exchangeRateDTO);
        var savedExchangeRateEntity = exchangeRateDAO.save(exchangeRateEntity);
        exchangeRateDTO.setId(savedExchangeRateEntity.getId());

        return exchangeRateDTO;
    }

    @Override
    public ExchangeRateDTO findByBaseAndTargetCurrenciesCode(String baseCurrencyCode, String targetCurrencyCode)
            throws WrapperException, SQLException {
        CurrencyEntity baseCurrency;
        CurrencyEntity targetCurrency;

        try {
            baseCurrency = currencyDAO.findByCode(baseCurrencyCode).get();
            targetCurrency = currencyDAO.findByCode(targetCurrencyCode).get();
        }
        catch (WrapperException e) {
            e.setErrorMessage("There is no currency or currencies with those codes.");
            throw e;
        }

        var exchangeRate = exchangeRateDAO
                .findByBaseAndTargetCurrenciesId(baseCurrency.getId(),
                            targetCurrency.getId());

        var exchangeRateDTO = new ExchangeRateDTO(exchangeRate.getId()
                , DtoEntityConvertor.convertCurrencyEntityToDto(baseCurrency)
                , DtoEntityConvertor.convertCurrencyEntityToDto(targetCurrency)
                , exchangeRate.getRate());

        return exchangeRateDTO;
    }

    @Override
    public List<ExchangeRateDTO> findAll() throws WrapperException, SQLException  {
        List<ExchangeRateEntity> exchangeRateEntityList = exchangeRateDAO.findAll();

        List<ExchangeRateDTO> exchangeRateDTOList = new ArrayList<>();
        for (ExchangeRateEntity exchangeRateEntity : exchangeRateEntityList) {
            var baseCurrency = currencyDAO.findById(exchangeRateEntity.getBaseCurrencyId()).get();
            var targetCurrency = currencyDAO.findById(exchangeRateEntity.getTargetCurrencyId()).get();

            ExchangeRateDTO exchangeRateDTO
                    = new ExchangeRateDTO(exchangeRateEntity.getId()
                    , DtoEntityConvertor.convertCurrencyEntityToDto(baseCurrency)
                    , DtoEntityConvertor.convertCurrencyEntityToDto(targetCurrency)
                    , exchangeRateEntity.getRate());

            exchangeRateDTOList.add(exchangeRateDTO);
        }

        return exchangeRateDTOList;
    }

    @Override
    public ExchangeRateDTO update(String baseCurrencyCode, String targetCurrencyCode, float rate)
            throws WrapperException, SQLException {
        CurrencyEntity baseCurrency;
        CurrencyEntity targetCurrency;
        ExchangeRateEntity exchangeRate;

        try {
            baseCurrency = currencyDAO.findByCode(baseCurrencyCode).get();
            targetCurrency = currencyDAO.findByCode(targetCurrencyCode).get();
            exchangeRate = exchangeRateDAO
                    .findByBaseAndTargetCurrenciesId(baseCurrency.getId(),
                            targetCurrency.getId());

            exchangeRateDAO.update(exchangeRate.getId(), rate);
        }
        catch (WrapperException e) {
            e.setErrorMessage("There is no currency pair with those codes in the database.");
            throw e;
        }

                ExchangeRateDTO exchangeRateDTO
                        = new ExchangeRateDTO(exchangeRate.getId()
                        , DtoEntityConvertor.convertCurrencyEntityToDto(baseCurrency)
                        , DtoEntityConvertor.convertCurrencyEntityToDto(targetCurrency)
                        , rate);

                return exchangeRateDTO;
    }
}
