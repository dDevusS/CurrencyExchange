package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO implements com.ddevus.currencyExchange.dao.interfaces.CurrencyDAO {

    private static final CurrencyDAO INSTANCE = new CurrencyDAO();

    private CurrencyDAO() {}

    public static CurrencyDAO getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public Currency save(Currency currency) {
        String sql = "INSERT INTO currencies (Code, FullName, Sing) VALUES (?, ?, ?)";

        try (var connection = ConnectionManager.open();
        var preparedStatement
                = connection.prepareStatement(sql, new String[]{"ID"})) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSing());

            try {
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                throw new SQLBadRequestException("Inserting currency failed" +
                        " due to currency with this code or sing exist in the database."
                        , SQLBadRequestException.ErrorReason.FAILED_INSERT);
            }

            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
                    if (resultSet.next()) {
                        currency.setId(resultSet.getInt(1));
                    } else {
                        throw new SQLBadRequestException("Inserting currency failed, no ID obtained."
                                , SQLBadRequestException.ErrorReason.FAILED_GET_LAST_OPERATION_ID);
                    }
                }
            }

            return currency;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    @Override
    public Currency findById(int id) {
        String sql = "SELECT * FROM currencies WHERE ID = ?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
               Currency currency = createCurrency(resultSet);

                return Optional.ofNullable(currency).get();
            }
            else {
                throw new SQLBadRequestException("There is no currency with this ID."
                        , SQLBadRequestException.ErrorReason.FAILED_FIND_OBJECT_IN_DB);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    @Override
    public Currency findByCode(String Code) {
        String sql = "SELECT * FROM currencies WHERE Code = ?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, Code);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Currency currency = createCurrency(resultSet);

                return Optional.ofNullable(currency).get();
            }
            else {
                throw new SQLBadRequestException("There is no currency with this Code."
                        , SQLBadRequestException.ErrorReason.FAILED_FIND_OBJECT_IN_DB);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    @Override
    public List<Currency> findAll() {
        String sql = "SELECT * FROM currencies";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();

            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(createCurrency(resultSet));
            }

            return currencies;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
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
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    private static Currency createCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("ID"),
                resultSet.getString("Code"),
                resultSet.getString("FullName"),
                resultSet.getString("Sing")
        );
    }
}
