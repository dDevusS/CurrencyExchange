package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.ExchangeDAO;
import com.ddevus.currencyExchange.dao.interfaces.IExchangeDAO;
import com.ddevus.currencyExchange.dto.ExchangeDTO;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.NoResultException;
import com.ddevus.currencyExchange.services.interfaces.IExchange_Service;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService implements IExchange_Service {

    private static final IExchangeDAO EXCHANGE_DAO = ExchangeDAO.getINSTANCE();
    @Getter
    private static final ExchangeService INSTANCE = new ExchangeService();

    private ExchangeService() {
    }

    @Override
    public ExchangeDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        var requiredExchangeRate
                = EXCHANGE_DAO.getRequiredExchangeRate(baseCurrencyCode, targetCurrencyCode);

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
