package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.CurrencyEntity;
import com.ddevus.currencyExchange.exceptions.DataBaseException;
import com.ddevus.currencyExchange.exceptions.SqlBadRequestException;
import com.ddevus.currencyExchange.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAOImplementation implements CurrencyDAO {

    private static final CurrencyDAOImplementation INSTANCE = new CurrencyDAOImplementation();

    private CurrencyDAOImplementation() {}

    public static CurrencyDAOImplementation getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public CurrencyEntity save(CurrencyEntity currency) throws DataBaseException, SqlBadRequestException {
        String sql = "INSERT INTO currencies (Code, FullName, Sing) VALUES (?, ?, ?)";

        try (var connection = ConnectionManager.open();
        var preparedStatement
                = connection.prepareStatement(sql, new String[]{"ID"})) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSing());

            if (preparedStatement.executeUpdate() == 0) {
                throw new SqlBadRequestException("Inserting currency failed, no rows affected.", SqlBadRequestException.ErrorReason.FAILED_INSERT);
            }

            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
                    if (resultSet.next()) {
                        currency.setId(resultSet.getInt(1));
                    } else {
                        throw new SqlBadRequestException("Inserting currency failed, no ID obtained.", SqlBadRequestException.ErrorReason.FAILED_GET_LAST_OPERATION_ID);
                    }
                }
            }

            return currency;
        }
        catch (SQLException e) {
            throw new DataBaseException("Error connecting to the database.", e);
        }
    }

    @Override
    public Optional<CurrencyEntity> findById(int id) {
        String sql = "SELECT * FROM currencies WHERE ID = ?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            CurrencyEntity currency = null;
            if (resultSet.next()) {
                currency = createCurrency(resultSet);
            }

            return Optional.ofNullable(currency);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CurrencyEntity> findByCode(String Code) {
        String sql = "SELECT * FROM currencies WHERE Code = ?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, Code);
            var resultSet = preparedStatement.executeQuery();

            CurrencyEntity currency = null;
            if (resultSet.next()) {
                currency = createCurrency(resultSet);
            }

            return Optional.ofNullable(currency);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CurrencyEntity> findAll() throws DataBaseException {
        String sql = "SELECT * FROM currencies";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();

            List<CurrencyEntity> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(createCurrency(resultSet));
            }
            return currencies;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private static CurrencyEntity createCurrency(ResultSet resultSet) throws SQLException {
        return new CurrencyEntity(
                resultSet.getInt("ID"),
                resultSet.getString("Code"),
                resultSet.getString("FullName"),
                resultSet.getString("Sing")
        );
    }
}
