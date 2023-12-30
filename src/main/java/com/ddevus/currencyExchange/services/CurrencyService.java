package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.entity.CurrencyEntity;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {

    public CurrencyEntity save(CurrencyEntity currency);

    public CurrencyEntity findById(int id);

    public List<CurrencyEntity> findAll();

    public boolean delete(int id);
}
