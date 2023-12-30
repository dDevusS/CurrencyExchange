package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAOImplementation;
import com.ddevus.currencyExchange.entity.CurrencyEntity;

import java.util.List;

public class CurrencyServiceImplementation implements CurrencyService{

    private final CurrencyDAOImplementation currencyDAO = CurrencyDAOImplementation.getINSTANCE();

    @Override
    public CurrencyEntity save(CurrencyEntity currency) {
        try {
            return currencyDAO.save(currency);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CurrencyEntity findById(int id) {
        try {
            var possibleCurrency = currencyDAO.findById(id);

            return possibleCurrency.get();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CurrencyEntity> findAll() {
        try {
            var currencies = currencyDAO.findAll();

            return currencies;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            var isDeleted = currencyDAO.delete(id);

            return isDeleted;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
