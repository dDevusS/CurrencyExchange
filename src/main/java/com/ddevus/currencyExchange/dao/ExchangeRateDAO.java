package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAO implements com.ddevus.currencyExchange.dao.interfaces.ExchangeRateDAO {

    private static CurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final ExchangeRateDAO INSTANCE = new ExchangeRateDAO();

    private ExchangeRateDAO() {}

    public static ExchangeRateDAO getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        String sql = "INSERT INTO exchangeRates (BaseCurrencyID, TargetCurrencyID, Rate) VALUES (?, ?, ?)";

        try (var connection = ConnectionManager.open();
             var preparedStatement
                     = connection.prepareStatement(sql, new String[]{"ID"})) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setFloat(3, exchangeRate.getRate());

            try {
                preparedStatement.executeUpdate();
            }
            catch (DatabaseException e) {
                throw new DatabaseException("Couldn't to connect to the database."
                        , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
            }
            catch (SQLException e) {
                throw new SQLBadRequestException("Inserting currency pair failed" +
                        " due to currency with this code or sing exist in the database."
                        , WrapperException.ErrorReason.FAILED_INSERT);
            }

            try (var statement = connection.createStatement();
                 var resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
                    if (resultSet.next()) {
                        exchangeRate.setId(resultSet.getInt(1));
                    } else {
                        throw new SQLBadRequestException("Inserting currency failed, no ID obtained."
                                , WrapperException.ErrorReason.FAILED_GET_LAST_OPERATION_ID);
                    }
            }

            return exchangeRate;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    @Override
    public ExchangeRate findByBaseAndTargetCurrencies(Currency baseCurrency, Currency targetCurrency) {
        String sql = "SELECT * FROM exchangeRates WHERE BaseCurrencyID=? AND TargetCurrencyID=?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, baseCurrency.getId());
            preparedStatement.setInt(2, targetCurrency.getId());
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new ExchangeRate(resultSet.getInt("ID")
                        , baseCurrency
                        , targetCurrency
                        , resultSet.getFloat("Rate"));
            }
            else {
                throw new SQLBadRequestException("There is not currency pair with those codes in database"
                        , WrapperException.ErrorReason.FAILED_FIND_OBJECT_IN_DB );
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    @Override
    public List<ExchangeRate> findAll() {
        String sql = "SELECT * FROM exchangeRates";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                var baseCurrency
                        = currencyDAO.findById(resultSet.getInt("BaseCurrencyID"));
                var targetCurrency
                        = currencyDAO.findById(resultSet.getInt("TargetCurrencyID"));

                ExchangeRate exchangeRate = new ExchangeRate(resultSet.getInt("ID")
                , baseCurrency
                , targetCurrency
                , resultSet.getFloat("Rate"));

                exchangeRates.add(exchangeRate);
            }

            return exchangeRates;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    @Override
    public boolean update(int id, float rate) {
        String sql = "UPDATE exchangeRates SET Rate=? WHERE ID=?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setFloat(1, rate);
            preparedStatement.setInt(2, id);
            var result = preparedStatement.executeUpdate() > 0;

            return result;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }
}
