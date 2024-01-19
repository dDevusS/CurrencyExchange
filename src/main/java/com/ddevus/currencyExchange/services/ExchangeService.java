package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.dao.interfaces.IExchangeRateDAO;
import com.ddevus.currencyExchange.dto.ExchangeDTO;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.NoResultException;
import com.ddevus.currencyExchange.services.interfaces.IExchange_Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

public class ExchangeService implements IExchange_Service {

    private static final IExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getINSTANCE();
    private static final Logger logger = Logger.getLogger(ExchangeService.class.getName());
    private static final ExchangeService INSTANCE = new ExchangeService();

    private ExchangeService() {
    }

    public static ExchangeService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {

        logger.info("Getting currency.");

        var requiredExchangeRate
                = exchangeRateDAO.getRequiredExchangeRate(baseCurrencyCode, targetCurrencyCode);

        if (requiredExchangeRate == null) {
            throw new NoResultException("There is no suitable exchange rate in the database for these currency pairs.");
        }

        return getExchangeDtoWithConvertedAmount(amount, requiredExchangeRate);
    }

    private ExchangeDTO getExchangeDtoWithConvertedAmount(BigDecimal amount, ExchangeRate exchangeRate) {
        BigDecimal convertAmount = amount.multiply(exchangeRate.getRate());
        convertAmount = convertAmount.setScale(2, RoundingMode.HALF_UP);

        return new ExchangeDTO(exchangeRate.getBaseCurrency()
                , exchangeRate.getTargetCurrency()
                , exchangeRate.getRate()
                , amount, convertAmount);
    }

}
