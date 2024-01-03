package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dto.CurrencyExchangerDTO;
import com.ddevus.currencyExchange.dto.ExchangeRateDTO;
import com.ddevus.currencyExchange.services.interfaces.CurrenciesExchangerService;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRateService;

public class CurrenciesExchangerServiceImplementation implements CurrenciesExchangerService {

    private final CurrencyService currencyService = CurrencyServiceImplementation.getINSTANCE();
    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImplementation.getINSTANCE();
    private static final CurrenciesExchangerService INSTANCE = new CurrenciesExchangerServiceImplementation();

    private CurrenciesExchangerServiceImplementation() {}

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
            var exchangeRateDTO
                    = exchangeRateService.findByBaseAndTargetCurrenciesCode(baseCurrencyCode, targetCurrencyCode);
            return convertBaseCurrencyToTargetCurrency(exchangeRateDTO, amount);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CurrencyExchangerDTO trySecondScript(String baseCurrencyCode, String targetCurrencyCode, float amount) {
        try {
            var inverseExchangeRateDTO
                    = exchangeRateService.findByBaseAndTargetCurrenciesCode(targetCurrencyCode, baseCurrencyCode);
            return convertBaseCurrencyToTargetCurrency(inverseExchangeRateDTO, amount);
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

    private CurrencyExchangerDTO convertBaseCurrencyToTargetCurrency (ExchangeRateDTO exchangeRateDTO, float amount) {
        float convertAmount = amount * exchangeRateDTO.getRate();
        var currencyExchangerDTO
                = new CurrencyExchangerDTO(exchangeRateDTO, amount, convertAmount);

        return currencyExchangerDTO;
    }
}
