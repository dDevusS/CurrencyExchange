package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.entity.Currency;

import java.util.List;

public interface CurrencyService {

    public Currency save(Currency currency);

    public Currency findById(int id);

    public Currency findByCode(String code);

    public List<Currency> findAll();

    public boolean delete(int id);
}
