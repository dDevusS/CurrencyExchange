package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.CurrencyEntity;
import com.ddevus.currencyExchange.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CurrencyDAOImplementation implements CurrencyDAO {

    private static final CurrencyDAOImplementation INSTANCE = new CurrencyDAOImplementation();

    private CurrencyDAOImplementation() {}

    public static CurrencyDAOImplementation getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public CurrencyEntity save(CurrencyEntity currency) {
        try (var connection = ConnectionManager.open();
        var preparedStatement
                = connection
                .prepareStatement("INSERT INTO currencies (Code, FullName, Sing) VALUES (?, ?, ?)"
                        , Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSing());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            currency.setId(generatedKeys.getInt("ID"));
            return currency;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        var preparedStatement
                = connection.prepareStatement("DELETE FROM currencies WHERE ID = ?")) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
