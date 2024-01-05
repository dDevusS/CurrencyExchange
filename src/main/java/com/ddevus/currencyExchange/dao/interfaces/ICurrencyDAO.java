package com.ddevus.currencyExchange.dao.interfaces;

import com.ddevus.currencyExchange.entity.Currency;

import java.util.List;

public interface ICurrencyDAO {

    public Currency save(Currency currency);

    public Currency findById(int id);

    public Currency findByCode(String Code);

    public List<Currency> findAll();

    public boolean deleteByCode(String code);
}
