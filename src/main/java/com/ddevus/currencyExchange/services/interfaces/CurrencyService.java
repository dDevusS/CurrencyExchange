package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {

    public CurrencyDTO save(CurrencyDTO currency);

    public CurrencyDTO findById(int id);

    public CurrencyDTO findByCode(String code);

    public List<CurrencyDTO> findAll();

    public boolean delete(int id);
}
