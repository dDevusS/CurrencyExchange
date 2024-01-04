package com.ddevus.currencyExchange.dao.interfaces;

import com.ddevus.currencyExchange.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDAO {

    public Currency save(Currency currency);

    public Optional<Currency> findById(int id);

    public Optional<Currency> findByCode(String Code);

    public List<Currency> findAll();

    public boolean delete(int id);
}
