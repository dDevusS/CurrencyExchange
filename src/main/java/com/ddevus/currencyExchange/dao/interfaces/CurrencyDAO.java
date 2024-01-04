package com.ddevus.currencyExchange.dao.interfaces;

import com.ddevus.currencyExchange.entity.Currency;

import java.util.List;

public interface CurrencyDAO {

    public Currency save(Currency currency);

    public Currency findById(int id);

    public Currency findByCode(String Code);

    public List<Currency> findAll();

    public boolean delete(int id);
}
