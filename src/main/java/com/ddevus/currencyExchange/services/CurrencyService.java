package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.services.interfaces.ICurrency_Service;

import java.util.List;

public class CurrencyService implements ICurrency_Service {

    private static final CurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final ICurrency_Service INSTANCE = new CurrencyService();

    private CurrencyService() {
    }

    public static ICurrency_Service getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public Currency save(Currency currency) {
        currency = currencyDAO.save(currency);

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
    public boolean deleteByCode(String code) {

        return currencyDAO.deleteByCode(code);
    }
}
