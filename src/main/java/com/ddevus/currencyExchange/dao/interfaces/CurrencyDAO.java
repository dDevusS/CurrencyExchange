package com.ddevus.currencyExchange.dao.interfaces;

import com.ddevus.currencyExchange.entity.CurrencyEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CurrencyDAO {

    public CurrencyEntity save(CurrencyEntity currency) throws SQLException;

    public Optional<CurrencyEntity> findById(int id) throws SQLException;

    public Optional<CurrencyEntity> findByCode(String Code) throws SQLException;

    public List<CurrencyEntity> findAll() throws SQLException;

    public boolean delete(int id) throws SQLException;
}
