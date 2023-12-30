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
        String sql = "INSERT INTO currencies (Code, FullName, Sing) VALUES (?, ?, ?)";

        try (var connection = ConnectionManager.open();
        var preparedStatement
                = connection.prepareStatement(sql, new String[]{"ID"})) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSing());

            if (preparedStatement.executeUpdate() == 0) {
                // TODO: change this exception
                throw new SQLException("Inserting currency failed, no rows affected.");
            }

            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
                    if (resultSet.next()) {
                        currency.setId(resultSet.getInt(1));
                    } else {
                        // TODO: измените этот exception
                        throw new SQLException("Inserting currency failed, no ID obtained.");
                    }
                }
            }

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
        String sql = "DELETE FROM currencies WHERE ID = ?";

        try (var connection = ConnectionManager.open();
        var preparedStatement
                = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
