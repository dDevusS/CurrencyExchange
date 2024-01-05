package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dto.CurrencyExchangerDTO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.services.interfaces.CurrenciesExchangerService;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRateService;

import java.util.List;

public class Exchange_Service implements CurrenciesExchangerService {

    private final CurrencyService currencyService = Currency_Service.getINSTANCE();
    private final ExchangeRateService exchangeRateService = ExchangeRate_Service.getINSTANCE();
    private static final Exchange_Service INSTANCE = new Exchange_Service();

    private Exchange_Service() {}

    public static Exchange_Service getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public CurrencyExchangerDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, float amount) {
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

        CurrencyExchangerDTO currencyExchangerDTO = tryFirstScript(baseCurrency, targetCurrency, amount);

        return currencyExchangerDTO;
    }

    private CurrencyExchangerDTO tryFirstScript(Currency baseCurrency, Currency targetCurrency, float amount) {
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

    private CurrencyExchangerDTO trySecondScript(Currency baseCurrency, Currency targetCurrency, float amount) {
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

    private CurrencyExchangerDTO tryThirdScript(Currency baseCurrency, Currency targetCurrency, float amount) {

        List<ExchangeRate> exchangeRateList = exchangeRateService.findAll();

        //TODO: Realize logic seaching optimal pair for exchanging





        return null;
    }

    private CurrencyExchangerDTO convertBaseCurrencyToTargetCurrency (ExchangeRate exchangeRate
            , float amount, boolean isInverted) {
        float convertAmount;

        if (!isInverted) {
            convertAmount = amount * exchangeRate.getRate();
        }
        else {
            convertAmount = amount / exchangeRate.getRate();
        }

        var currencyExchanger
                = new CurrencyExchangerDTO(exchangeRate, amount, convertAmount);

        return currencyExchanger;
    }
}
