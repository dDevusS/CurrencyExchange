package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.entity.Currency;

import java.util.List;

public class Currency_Service implements com.ddevus.currencyExchange.services.interfaces.ICurrency_Service {

    private static final CurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final com.ddevus.currencyExchange.services.interfaces.ICurrency_Service INSTANCE = new Currency_Service();

    private Currency_Service() {
    }

    public static com.ddevus.currencyExchange.services.interfaces.ICurrency_Service getINSTANCE() {
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

        return currencyDAO.findById(id);
    }

    @Override
    public Currency findByCode(String code) {

        return currencyDAO.findByCode(code);
    }

    @Override
    public List<Currency> findAll() {

        return currencyDAO.findAll();
    }

    @Override
    public boolean delete(int id) {

        return currencyDAO.delete(id);
    }
}
