package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.entity.CurrencyEntity;

import java.util.List;

public interface CurrencyService {

    public CurrencyEntity save(CurrencyEntity currency);

    public CurrencyEntity findById(int id);

    public CurrencyEntity findByCode(String code);

    public List<CurrencyEntity> findAll();

    public boolean delete(int id);
}
