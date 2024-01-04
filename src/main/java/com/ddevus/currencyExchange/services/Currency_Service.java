package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;

import java.util.List;

public class Currency_Service implements CurrencyService {

    private static final com.ddevus.currencyExchange.dao.interfaces.CurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final CurrencyService INSTANCE = new Currency_Service();

    private Currency_Service() {}

    public static CurrencyService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public Currency save(Currency currency) {
            currency = currencyDAO.save(currency);
            currency.setId(currency.getId());

            return currency;
    }

    @Override
    public Currency findById(int id) {
        var currency = currencyDAO.findById(id);

        return currency;
    }

    @Override
    public Currency findByCode(String code) {
        var currency = currencyDAO.findByCode(code);

        return currency;
    }

    @Override
    public List<Currency> findAll() {
        var currencyList = currencyDAO.findAll();

        return currencyList;
    }

    @Override
    public boolean delete(int id) {
            var isDeleted = currencyDAO.delete(id);

            return isDeleted;
    }
}
