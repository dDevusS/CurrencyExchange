package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAO implements com.ddevus.currencyExchange.dao.interfaces.ExchangeRateDAO {

    private static final ExchangeRateDAO INSTANCE = new ExchangeRateDAO();

    private ExchangeRateDAO() {}

    public static ExchangeRateDAO getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) throws WrapperException {
        String sql = "INSERT INTO exchangeRates (BaseCurrencyID, TargetCurrencyID, Rate) VALUES (?, ?, ?)";

        try (var connection = ConnectionManager.open();
             var preparedStatement
                     = connection.prepareStatement(sql, new String[]{"ID"})) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setFloat(3, exchangeRate.getRate());

            // TODO: Add checking existed inverted pair



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
    public ExchangeRate findByBaseAndTargetCurrenciesId(int baseCurrencyId, int targetCurrencyId)
            throws WrapperException {
        String sql = "SELECT * FROM exchangeRates WHERE BaseCurrencyID=? AND TargetCurrencyID=?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ExchangeRate exchangeRate = createExchangeRate(resultSet);

                return exchangeRate;
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
    public List<ExchangeRate> findAll() throws WrapperException {
        String sql = "SELECT * FROM exchangeRates";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(createExchangeRate(resultSet));
            }
            return exchangeRates;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    @Override
    public boolean update(int id, float rate) throws WrapperException {
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

    private static ExchangeRate createExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(resultSet.getInt("ID")
                , resultSet.getInt("BaseCurrencyID")
                , resultSet.getInt("TargetCurrencyID")
                , resultSet.getFloat("Rate"));
    }
}
