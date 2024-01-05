package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dto.ExchangeDTO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.services.interfaces.Currencies_ExchangerService;
import com.ddevus.currencyExchange.services.interfaces.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRate_Service;

import java.util.List;

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

            return convertBaseCurrencyToTargetCurrency(exchangeRate, amount, false);
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

            return convertBaseCurrencyToTargetCurrency(inverseExchangeRate, amount, true);
        }
        catch (SQLBadRequestException e) {

            return tryThirdScript(baseCurrency, targetCurrency, amount);
        }
    }

    private ExchangeDTO tryThirdScript(Currency baseCurrency, Currency targetCurrency, float amount) {

        List<ExchangeRate> exchangeRateList = exchangeRateService.findAll();

        //TODO: Realize logic seaching optimal pair for exchanging





        return null;
    }

    private ExchangeDTO convertBaseCurrencyToTargetCurrency (ExchangeRate exchangeRate
            , float amount, boolean isInverted) {
        float convertAmount;

        if (!isInverted) {
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
