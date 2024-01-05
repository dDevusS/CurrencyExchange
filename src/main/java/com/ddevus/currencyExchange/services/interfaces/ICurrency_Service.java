package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.entity.Currency;

import java.util.List;

public interface ICurrency_Service {

    public Currency save(Currency currency);

    public Currency findById(int id);

    public Currency findByCode(String code);

    public List<Currency> findAll();

    public boolean deleteByCode(String code);
}
