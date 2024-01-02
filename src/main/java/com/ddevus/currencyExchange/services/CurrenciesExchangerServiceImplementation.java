package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dao.CurrencyDAOImplementation;
import com.ddevus.currencyExchange.dao.ExchangeRateDAO;
import com.ddevus.currencyExchange.dao.ExchangeRateDAOImplementation;
import com.ddevus.currencyExchange.dto.CurrencyExchangerDTO;

public class CurrenciesExchangerServiceImplementation implements CurrenciesExchangerService {

    private final CurrencyDAO currencyDAO = CurrencyDAOImplementation.getINSTANCE();
    private final ExchangeRateDAO exchangeRateDAO = ExchangeRateDAOImplementation.getINSTANCE();
    private static final CurrenciesExchangerService INSTANCE = new CurrenciesExchangerServiceImplementation();

    private CurrenciesExchangerServiceImplementation() {}

    public static CurrenciesExchangerService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public CurrencyExchangerDTO exchangeAmount(String baseCurrencyCode, String targetCurrencyCode, float amount) {









        return null;
    }
}
