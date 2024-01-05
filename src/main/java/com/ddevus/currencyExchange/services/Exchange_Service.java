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

public class Exchange_Service implements Currencies_ExchangerService {

    private final Currency_Service currencyService = com.ddevus.currencyExchange.services.Currency_Service.getINSTANCE();
    private final ExchangeRate_Service exchangeRateService = com.ddevus.currencyExchange.services.ExchangeRate_Service.getINSTANCE();
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

            return convertBaseCurrencyToTargetCurrency(baseCurrency, amount, exchangeRate);
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

            return convertBaseCurrencyToTargetCurrency(baseCurrency, amount, inverseExchangeRate);
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
                if (isThere(baseID, exchangeRate)) {
                    exchangeRateSet.add(exchangeRate);
                }
            }

            for (ExchangeRate exchangeRate: exchangeRateSet) {
                int transID = getAnotherID(baseID, exchangeRate);

                for (ExchangeRate targetExchangeRate : exchangeRateList) {
                    if (isGoal(targetID, transID, targetExchangeRate)) {
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

                return convertBaseCurrencyToTargetCurrency(baseCurrency, amount, exchangeRate);
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

    private static boolean isThere(int baseID, ExchangeRate exchangeRate) {
        return baseID == exchangeRate.getBaseCurrency().getId() || baseID == exchangeRate.getTargetCurrency().getId();
    }

    private static boolean isGoal(int baseID, int targetID, ExchangeRate exchangeRate) {
        return isThere(baseID, exchangeRate) && isThere(targetID, exchangeRate);
    }

    private static int getAnotherID(int id, ExchangeRate exchangeRate) {
        if (id != exchangeRate.getBaseCurrency().getId()) {
            return exchangeRate.getBaseCurrency().getId();
        }
        else {
            return exchangeRate.getTargetCurrency().getId();
        }
    }

    private ExchangeDTO convertBaseCurrencyToTargetCurrency (Currency fromCurrency, float amount
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
