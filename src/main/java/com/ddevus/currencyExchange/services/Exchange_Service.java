package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dto.ExchangeDTO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.interfaces.ICurrency_Service;
import com.ddevus.currencyExchange.services.interfaces.IExchangeRate_Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Exchange_Service implements com.ddevus.currencyExchange.services.interfaces.IExchange_Service {

    private final ICurrency_Service currencyService
            = Currency_Service.getINSTANCE();
    private final IExchangeRate_Service exchangeRateService
            = ExchangeRate_Service.getINSTANCE();
    private final Logger logger = Logger.getLogger(Exchange_Service.class.getName());
    private static final Exchange_Service INSTANCE = new Exchange_Service();

    private Exchange_Service() {
    }

    public static Exchange_Service getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        Currency baseCurrency;
        Currency targetCurrency;

        logger.info("Getting currency.");
        try {
            baseCurrency = currencyService.findByCode(baseCurrencyCode);
            targetCurrency = currencyService.findByCode(targetCurrencyCode);
        }
        catch (SQLBadRequestException e) {
            e.setErrorMessage("There are no currencies with those codes in the database.");
            throw e;
        }

        return tryFirstScript(baseCurrency, targetCurrency, amount);
    }

    private ExchangeDTO tryFirstScript(Currency baseCurrency, Currency targetCurrency, BigDecimal amount) {
        logger.info("Running first script for exchanging.");

        try {
            var exchangeRate
                    = exchangeRateService.findByBaseAndTargetCurrenciesCode(baseCurrency.getCode()
                    , targetCurrency.getCode());

            return getExchangeDtoWithConvertedAmount(baseCurrency, amount, exchangeRate);
        }
        catch (SQLBadRequestException e) {

            return trySecondScript(baseCurrency, targetCurrency, amount);
        }
    }

    private ExchangeDTO trySecondScript(Currency baseCurrency, Currency targetCurrency, BigDecimal amount) {
        logger.info("Running second script for exchanging.");

        try {
            var inverseExchangeRate = exchangeRateService
                    .findByBaseAndTargetCurrenciesCode(targetCurrency.getCode()
                            , baseCurrency.getCode());

            return getExchangeDtoWithConvertedAmount(baseCurrency, amount, inverseExchangeRate);
        }
        catch (SQLBadRequestException e) {

            return tryThirdScript(baseCurrency, targetCurrency, amount);
        }
    }

    private ExchangeDTO tryThirdScript(Currency baseCurrency, Currency targetCurrency, BigDecimal amount) {
        logger.info("Running third script for exchanging.");

        List<ExchangeRate> exchangeRateList = exchangeRateService.findAll();
        ExchangeRate transExchangeRate = null;
        ExchangeRate goalExchangeRate = null;

        Set<ExchangeRate> exchangeRateSet = new LinkedHashSet<>();

        for (ExchangeRate exchangeRate : exchangeRateList) {
            if (isThere(baseCurrency, exchangeRate)) {
                exchangeRateSet.add(exchangeRate);
            }
        }

        for (ExchangeRate exchangeRate : exchangeRateSet) {
            Currency transCurrency = getAnotherCurrency(baseCurrency, exchangeRate);

            for (ExchangeRate targetExchangeRate : exchangeRateList) {
                if (isGoal(targetCurrency, transCurrency, targetExchangeRate)) {
                    goalExchangeRate = targetExchangeRate;
                    transExchangeRate = exchangeRate;
                    break;
                }
            }

            if (goalExchangeRate != null) {
                break;
            }
        }

        if (goalExchangeRate == null) {
            throw new SQLBadRequestException("There is no suitable exchange rate in the database for these currency pairs."
                    , WrapperException.ErrorReason.FAILED_FIND_EXCHANGE_RATE_IN_DB);
        }
        else {
            BigDecimal goalRate = getGoalRate(baseCurrency, targetCurrency, transExchangeRate, goalExchangeRate);
            ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, goalRate);

            return getExchangeDtoWithConvertedAmount(baseCurrency, amount, exchangeRate);
        }
    }

    private static BigDecimal getGoalRate(Currency baseCurrency, Currency targetCurrency
            , ExchangeRate transExchangeRate, ExchangeRate goalExchangeRate) {
        BigDecimal goalRate;

        if (baseCurrency.getId() == transExchangeRate.getBaseCurrency().getId()) {
            goalRate = transExchangeRate.getRate();
        }
        else {
            goalRate = BigDecimal.valueOf(1).divide(transExchangeRate.getRate()
                    , 6, RoundingMode.HALF_UP);
        }

        if (targetCurrency.getId() == goalExchangeRate.getTargetCurrency().getId()) {
            goalRate = goalRate.multiply(goalExchangeRate.getRate());
            goalRate = goalRate.setScale(6, RoundingMode.HALF_UP);
        }
        else {
            goalRate = goalRate.divide(goalExchangeRate.getRate(), 6, RoundingMode.HALF_UP);
        }

        return goalRate;
    }

    private static boolean isThere(Currency baseCurrency, ExchangeRate exchangeRate) {
        return baseCurrency.getId() == exchangeRate.getBaseCurrency().getId() || baseCurrency.getId() == exchangeRate.getTargetCurrency().getId();
    }

    private static boolean isGoal(Currency baseCurrency, Currency targetCurrency, ExchangeRate exchangeRate) {
        return isThere(baseCurrency, exchangeRate) && isThere(targetCurrency, exchangeRate);
    }

    private static Currency getAnotherCurrency(Currency currency, ExchangeRate exchangeRate) {
        if (currency.getId() != exchangeRate.getBaseCurrency().getId()) {
            return exchangeRate.getBaseCurrency();
        }
        else {

            return exchangeRate.getTargetCurrency();
        }
    }

    private ExchangeDTO getExchangeDtoWithConvertedAmount(Currency fromCurrency, BigDecimal amount
            , ExchangeRate exchangeRate) {
        BigDecimal convertAmount;

        if (exchangeRate.getBaseCurrency().getId() == fromCurrency.getId()) {
            convertAmount = amount.multiply(exchangeRate.getRate());
            convertAmount = convertAmount.setScale(2, RoundingMode.HALF_UP);
        }
        else {
            convertAmount = amount.divide(exchangeRate.getRate(), 2, RoundingMode.HALF_UP);
        }

        return new ExchangeDTO(exchangeRate, amount, convertAmount);
    }
}
