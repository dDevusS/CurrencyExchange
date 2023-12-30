package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.ExchangeRateEntity;
import com.ddevus.currencyExchange.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAOImplementation implements ExchangeRateDAO {

    private static final ExchangeRateDAOImplementation INSTANCE = new ExchangeRateDAOImplementation();

    private ExchangeRateDAOImplementation() {}

    public static ExchangeRateDAOImplementation getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateEntity save(ExchangeRateEntity exchangeRate) {
        String sql = "INSERT INTO exchangeRates (BaseCurrencyID, TargetCurrencyID, Rate) VALUES (?, ?, ?)";

        try (var connection = ConnectionManager.open();
             var preparedStatement
                     = connection.prepareStatement(sql, new String[]{"ID"})) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setFloat(3, exchangeRate.getRate());

            if (preparedStatement.executeUpdate() == 0) {
                // TODO: change this exception
                throw new SQLException("Inserting currency failed, no rows affected.");
            }

            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
                    if (resultSet.next()) {
                        exchangeRate.setId(resultSet.getInt(1));
                    } else {
                        // TODO: измените этот exception
                        throw new SQLException("Inserting currency failed, no ID obtained.");
                    }
                }
            }

            return exchangeRate;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRateEntity findByBaseAndTargetCurrenciesId(int baseCurrencyId, int targetCurrencyId) {
        String sql = "SELECT * FROM exchangeRates WHERE BaseCurrencyID=? AND TargetCurrencyID=?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ExchangeRateEntity exchangeRate = createExchangeRate(resultSet);

                return exchangeRate;
            }
            else {
                throw new RuntimeException("Where is not ExchangeRate with those codes in database");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ExchangeRateEntity> findAll() {
        String sql = "SELECT * FROM exchangeRates";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();

            List<ExchangeRateEntity> exchangeRates = new ArrayList<ExchangeRateEntity>();
            while (resultSet.next()) {
                exchangeRates.add(createExchangeRate(resultSet));
            }
            return exchangeRates;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    private static ExchangeRateEntity createExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRateEntity(resultSet.getInt("ID")
                , resultSet.getInt("BaseCurrencyID")
                , resultSet.getInt("TargetCurrencyID")
                , resultSet.getFloat("Rate"));
    }
}
