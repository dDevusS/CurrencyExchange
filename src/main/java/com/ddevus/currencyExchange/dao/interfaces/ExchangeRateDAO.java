package com.ddevus.currencyExchange.dao.interfaces;

import com.ddevus.currencyExchange.entity.ExchangeRateEntity;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDAO {

    public ExchangeRateEntity save(ExchangeRateEntity exchangeRate) throws SQLException;

    public ExchangeRateEntity findByBaseAndTargetCurrenciesId(int baseCurrencyId, int targetCurrencyId) throws SQLException;

    public List<ExchangeRateEntity> findAll() throws SQLException;

    public boolean update(int id, float rate) throws SQLException;

}
