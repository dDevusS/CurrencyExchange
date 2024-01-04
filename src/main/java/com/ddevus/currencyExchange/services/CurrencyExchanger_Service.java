package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dto.CurrencyExchangerDTO;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.services.interfaces.CurrenciesExchangerService;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRateService;

public class CurrencyExchanger_Service implements CurrenciesExchangerService {

    private final CurrencyService currencyService = Currency_Service.getINSTANCE();
    private final ExchangeRateService exchangeRateService = ExchangeRate_Service.getINSTANCE();
    private static final CurrenciesExchangerService INSTANCE = new CurrencyExchanger_Service();

    private CurrencyExchanger_Service() {}

    public static CurrenciesExchangerService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public CurrencyExchangerDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, float amount) {
        try {
            var currencyExchangerDTO
                    = tryFirstScript(baseCurrencyCode, targetCurrencyCode, amount);
            return currencyExchangerDTO;
        }
        catch (Exception e) {

        }

        try {
            var currencyExchangerDTO
                    = trySecondScript(baseCurrencyCode, targetCurrencyCode, amount);
            return currencyExchangerDTO;
        }
        catch (Exception e) {

        }

        try {
            var currencyExchangerDTO
                    = tryThirdScript(baseCurrencyCode, targetCurrencyCode, amount);
            return currencyExchangerDTO;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CurrencyExchangerDTO tryFirstScript(String baseCurrencyCode, String targetCurrencyCode, float amount) {
        try {
            var exchangeRate
                    = exchangeRateService.findByBaseAndTargetCurrenciesCode(baseCurrencyCode, targetCurrencyCode);
            return convertBaseCurrencyToTargetCurrency(exchangeRate, amount);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CurrencyExchangerDTO trySecondScript(String baseCurrencyCode, String targetCurrencyCode, float amount) {
        try {
            var inverseExchangeRate
                    = exchangeRateService.findByBaseAndTargetCurrenciesCode(targetCurrencyCode, baseCurrencyCode);
            return convertBaseCurrencyToTargetCurrency(inverseExchangeRate, amount);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CurrencyExchangerDTO tryThirdScript(String baseCurrencyCode, String targetCurrencyCode, float amount) {
        try {
            var baseCurrencyDTO = currencyService.findByCode(baseCurrencyCode);
            var targetCurrencyDTO = currencyService.findByCode(targetCurrencyCode);

            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CurrencyExchangerDTO convertBaseCurrencyToTargetCurrency (ExchangeRate exchangeRate, float amount) {
        float convertAmount = amount * exchangeRate.getRate();
        var currencyExchanger
                = new CurrencyExchangerDTO(exchangeRate, amount, convertAmount);

        return currencyExchanger;
    }
}
