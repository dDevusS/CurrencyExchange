package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.dto.ExchangeRateDTO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRateService;
import com.ddevus.currencyExchange.utils.DtoEntityConvertor;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRate_Service implements ExchangeRateService {

    private static final com.ddevus.currencyExchange.dao.interfaces.ExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getINSTANCE();
    private static final com.ddevus.currencyExchange.dao.interfaces.CurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final ExchangeRateService INSTANCE= new ExchangeRate_Service();

    private ExchangeRate_Service() {}

    public static ExchangeRateService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateDTO save(ExchangeRateDTO exchangeRateDTO) throws WrapperException {
        var exchangeRateEntity = DtoEntityConvertor.convertExchangeRateDtoToEntity(exchangeRateDTO);
        var savedExchangeRateEntity = exchangeRateDAO.save(exchangeRateEntity);
        exchangeRateDTO.setId(savedExchangeRateEntity.getId());

        return exchangeRateDTO;
    }

    @Override
    public ExchangeRateDTO findByBaseAndTargetCurrenciesCode(String baseCurrencyCode, String targetCurrencyCode)
            throws WrapperException {
        Currency baseCurrency;
        Currency targetCurrency;

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
    public List<ExchangeRateDTO> findAll() throws WrapperException  {
        List<ExchangeRate> exchangeRateList = exchangeRateDAO.findAll();

        List<ExchangeRateDTO> exchangeRateDTOList = new ArrayList<>();
        for (ExchangeRate exchangeRate : exchangeRateList) {
            var baseCurrency = currencyDAO.findById(exchangeRate.getBaseCurrencyId()).get();
            var targetCurrency = currencyDAO.findById(exchangeRate.getTargetCurrencyId()).get();

            ExchangeRateDTO exchangeRateDTO
                    = new ExchangeRateDTO(exchangeRate.getId()
                    , DtoEntityConvertor.convertCurrencyEntityToDto(baseCurrency)
                    , DtoEntityConvertor.convertCurrencyEntityToDto(targetCurrency)
                    , exchangeRate.getRate());

            exchangeRateDTOList.add(exchangeRateDTO);
        }

        return exchangeRateDTOList;
    }

    @Override
    public ExchangeRateDTO update(String baseCurrencyCode, String targetCurrencyCode, float rate)
            throws WrapperException {
        Currency baseCurrency;
        Currency targetCurrency;
        ExchangeRate exchangeRate;

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
