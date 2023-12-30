package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.CurrencyEntity;

import java.util.List;
import java.util.Optional;

public interface CurrencyDAO {

    public CurrencyEntity save(CurrencyEntity currency);

    public Optional<CurrencyEntity> findById(int id);

    public Optional<CurrencyEntity> findByCode(String Code);

    public List<CurrencyEntity> findAll();

    public boolean delete(int id);
}
