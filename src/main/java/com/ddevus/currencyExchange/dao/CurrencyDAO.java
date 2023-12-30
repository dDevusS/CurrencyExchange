package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.CurrencyEntity;

import java.util.List;

public interface CurrencyDAO {

    public CurrencyEntity save(CurrencyEntity currency);

    public CurrencyEntity findById(int id);

    public List<CurrencyEntity> findAll();

    public boolean delete(int id);
}
