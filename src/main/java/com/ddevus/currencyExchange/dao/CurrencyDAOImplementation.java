package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.CurrencyEntity;
import com.ddevus.currencyExchange.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.List;

public class CurrencyDAOImplementation implements CurrencyDAO {

    private final CurrencyDAOImplementation INSTANCE = new CurrencyDAOImplementation();

    private CurrencyDAOImplementation() {}

    public CurrencyDAOImplementation getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public CurrencyEntity save(CurrencyEntity currency) {
        return null;
    }

    @Override
    public CurrencyEntity findById(int id) {
        return null;
    }

    @Override
    public List<CurrencyEntity> findAll() {
        return null;
    }

    @Override
    public boolean delete(int id) {
        try (var connection = ConnectionManager.open();
        var prepareStatement
                = connection.prepareStatement("DELETE FROM currencies WHERE ID = ?")) {
            prepareStatement.setInt(1, id);

            return prepareStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
