package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.NoResultException;
import com.ddevus.currencyExchange.utils.ConnectionManager;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAO implements com.ddevus.currencyExchange.dao.interfaces.IExchangeRateDAO {

    private static final CurrencyDAO CURRENCY_DAO = CurrencyDAO.getINSTANCE();
    @Getter
    private static final ExchangeRateDAO INSTANCE = new ExchangeRateDAO();

    private ExchangeRateDAO() {
    }

    @Override
    public ExchangeRate save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        String sql = "INSERT INTO exchangeRates (BaseCurrencyID, TargetCurrencyID, Rate) VALUES (?, ?, ?)";

        Currency baseCurrency = CURRENCY_DAO.findByCode(baseCurrencyCode);
        Currency targetCurrency = CURRENCY_DAO.findByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new NoResultException("There are no currencies with those codes in the database." +
                    " Inserting new exchange rate was failed.");
        }

        try (var connection = ConnectionManager.open();
             var preparedStatement
                     = connection.prepareStatement(sql, new String[]{"ID"})) {
            preparedStatement.setInt(1, baseCurrency.getId());
            preparedStatement.setInt(2, targetCurrency.getId());
            preparedStatement.setBigDecimal(3, rate);

            try {
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                return null;
            }

            try (var statement = connection.createStatement();
                 var resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
                if (resultSet.next()) {
                    return new ExchangeRate(resultSet.getInt("ID")
                            , baseCurrency
                            , targetCurrency
                            , rate);
                }
                else {
                    return null;
                }
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database.");
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
                        = CURRENCY_DAO.findById(resultSet.getInt("BaseCurrencyID"));
                var targetCurrency
                        = CURRENCY_DAO.findById(resultSet.getInt("TargetCurrencyID"));

                ExchangeRate exchangeRate = new ExchangeRate(resultSet.getInt("ID")
                        , baseCurrency
                        , targetCurrency
                        , resultSet.getBigDecimal("Rate"));

                exchangeRates.add(exchangeRate);
            }

            return exchangeRates;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database.");
        }
    }

    @Override
    public boolean update(int id, BigDecimal rate) {
        String sql = "UPDATE exchangeRates SET Rate=? WHERE ID=?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setInt(2, id);

            return preparedStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database.");
        }
    }

    @Override
    public ExchangeRate findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        Currency baseCurrency = CURRENCY_DAO.findByCode(baseCurrencyCode);
        Currency targetCurrency = CURRENCY_DAO.findByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new NoResultException("There are no currencies with those codes in the database.");
        }

        return findByBaseAndTargetCurrencies(baseCurrency, targetCurrency);
    }

    private ExchangeRate findByBaseAndTargetCurrencies(Currency baseCurrency, Currency targetCurrency) {
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
                        , resultSet.getBigDecimal("Rate"));
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database.");
        }
    }

}
