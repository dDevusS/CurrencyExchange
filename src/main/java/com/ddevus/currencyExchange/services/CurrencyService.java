package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.services.interfaces.ICurrency_Service;
import lombok.Getter;

import java.util.List;

public class CurrencyService implements ICurrency_Service {

    private static final CurrencyDAO CURRENCY_DAO = CurrencyDAO.getINSTANCE();
    @Getter
    private static final ICurrency_Service INSTANCE = new CurrencyService();

    private CurrencyService() {
    }

    @Override
    public Currency save(Currency currency) {
        currency = CURRENCY_DAO.save(currency);

        return currency;
    }

    @Override
    public Currency findById(int id) {

        return CURRENCY_DAO.findById(id);
    }

    @Override
    public Currency findByCode(String code) {

        return CURRENCY_DAO.findByCode(code);
    }

    @Override
    public List<Currency> findAll() {

        return CURRENCY_DAO.findAll();
    }

    @Override
    public boolean deleteByCode(String code) {

        return CURRENCY_DAO.deleteByCode(code);
    }
}
