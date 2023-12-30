package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.CurrencyEntity;

import java.util.List;

public class CurrencyDAOImplementation implements CurrencyDAO {

    private CurrencyDAOImplementation currencyDAO = new CurrencyDAOImplementation();

    private CurrencyDAOImplementation() {}

    public CurrencyDAOImplementation getCurrencyDAO() {
        return currencyDAO;
    }

    @Override
    public CurrencyEntity save(CurrencyEntity currency) {
        return null;
    }

    @Override
    public CurrencyEntity findById(int id) {
        return null;
    }

    @Override
    public List<CurrencyEntity> findAll() {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}
