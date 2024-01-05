package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.entity.Currency;

import java.util.List;

public class Currency_Service implements com.ddevus.currencyExchange.services.interfaces.Currency_Service {

    private static final com.ddevus.currencyExchange.dao.interfaces.CurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final com.ddevus.currencyExchange.services.interfaces.Currency_Service INSTANCE = new Currency_Service();

    private Currency_Service() {}

    public static com.ddevus.currencyExchange.services.interfaces.Currency_Service getINSTANCE() {
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
