package com.ddevus.currencyExchange.services.interfaces;

import com.ddevus.currencyExchange.dto.CurrencyDTO;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyService {

    public CurrencyDTO save(CurrencyDTO currency) throws SQLException;

    public CurrencyDTO findById(int id) throws SQLException;

    public CurrencyDTO findByCode(String code) throws SQLException;

    public List<CurrencyDTO> findAll() throws SQLException;

    public boolean delete(int id) throws SQLException;
}
