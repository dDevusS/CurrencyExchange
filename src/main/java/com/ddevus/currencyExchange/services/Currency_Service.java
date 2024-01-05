package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.entity.Currency;

import java.util.List;

public class Currency_Service implements com.ddevus.currencyExchange.services.interfaces.ICurrency_Service {

    private static final CurrencyDAO I_CURRENCY_DAO = CurrencyDAO.getINSTANCE();
    private static final com.ddevus.currencyExchange.services.interfaces.ICurrency_Service INSTANCE = new Currency_Service();

    private Currency_Service() {}

    public static com.ddevus.currencyExchange.services.interfaces.ICurrency_Service getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public Currency save(Currency currency) {
        currency = I_CURRENCY_DAO.save(currency);
        currency.setId(currency.getId());

            return currency;
    }

    @Override
    public Currency findById(int id) {
        var currency = I_CURRENCY_DAO.findById(id);

        return currency;
    }

    @Override
    public Currency findByCode(String code) {
        var currency = I_CURRENCY_DAO.findByCode(code);

        return currency;
    }

    @Override
    public List<Currency> findAll() {
        var currencyList = I_CURRENCY_DAO.findAll();

        return currencyList;
    }

    @Override
    public boolean delete(int id) {
            var isDeleted = I_CURRENCY_DAO.delete(id);

            return isDeleted;
    }
}
