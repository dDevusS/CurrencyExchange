package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dto.ExchangeDTO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.interfaces.Currencies_ExchangerService;
import com.ddevus.currencyExchange.services.interfaces.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRate_Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Exchange_Service implements Currencies_ExchangerService {

    private final Currency_Service currencyService
            = com.ddevus.currencyExchange.services.Currency_Service.getINSTANCE();
    private final ExchangeRate_Service exchangeRateService
            = com.ddevus.currencyExchange.services.ExchangeRate_Service.getINSTANCE();
    private final Logger logger = Logger.getLogger(Exchange_Service.class.getName());
    private static final Exchange_Service INSTANCE = new Exchange_Service();

    private Exchange_Service() {}

    public static Exchange_Service getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, float amount) {
        Currency baseCurrency;
        Currency targetCurrency;

        try {
            baseCurrency = currencyService.findByCode(baseCurrencyCode);
            targetCurrency = currencyService.findByCode(targetCurrencyCode);
        }
        catch (SQLBadRequestException e) {
            e.setErrorMessage("There are no currencies with those codes in the database.");
            throw e;
        }

        ExchangeDTO exchangeDTO = tryFirstScript(baseCurrency, targetCurrency, amount);

        return exchangeDTO;
    }

    private ExchangeDTO tryFirstScript(Currency baseCurrency, Currency targetCurrency, float amount) {
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

    private ExchangeDTO trySecondScript(Currency baseCurrency, Currency targetCurrency, float amount) {
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

    private ExchangeDTO tryThirdScript(Currency baseCurrency, Currency targetCurrency, float amount) {

        List<ExchangeRate> exchangeRateList = exchangeRateService.findAll();
        int baseID = baseCurrency.getId();
        int targetID = targetCurrency.getId();
        ExchangeRate transExchangeRate = null;
        ExchangeRate goalExchangeRate = null;
        ExchangeDTO exchangeDTO;

        Set<ExchangeRate> exchangeRateSet = new LinkedHashSet<>();

            for (ExchangeRate exchangeRate : exchangeRateList) {
                if (isThere(baseCurrency, exchangeRate)) {
                    exchangeRateSet.add(exchangeRate);
                }
            }

            for (ExchangeRate exchangeRate: exchangeRateSet) {
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
                float goalRate = getGoalRate(baseCurrency, targetCurrency, transExchangeRate, goalExchangeRate);
                ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, goalRate);

                return getExchangeDtoWithConvertedAmount(baseCurrency, amount, exchangeRate);
            }
    }

    private static float getGoalRate(Currency baseCurrency, Currency targetCurrency
            , ExchangeRate transExchangeRate, ExchangeRate goalExchangeRate) {
        float goalRate = 0;

        if (baseCurrency.getId() == transExchangeRate.getBaseCurrency().getId()) {
            goalRate = transExchangeRate.getRate();
        }
        else {
            goalRate = 1 / transExchangeRate.getRate();
        }

        if (targetCurrency.getId() == goalExchangeRate.getTargetCurrency().getId()) {
            goalRate = goalRate * goalExchangeRate.getRate();
        }
        else {
            goalRate = goalRate / goalExchangeRate.getRate();
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

    private ExchangeDTO getExchangeDtoWithConvertedAmount(Currency fromCurrency, float amount
            , ExchangeRate exchangeRate) {
        float convertAmount;

        if (exchangeRate.getBaseCurrency().getId() == fromCurrency.getId()) {
            convertAmount = amount * exchangeRate.getRate();
        }
        else {
            convertAmount = amount / exchangeRate.getRate();
        }

        var currencyExchanger
                = new ExchangeDTO(exchangeRate, amount, convertAmount);

        return currencyExchanger;
    }
}
