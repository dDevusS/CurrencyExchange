package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.utils.ConnectionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExchangeRateDAO implements com.ddevus.currencyExchange.dao.interfaces.IExchangeRateDAO {

    private static final CurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final ExchangeRateDAO INSTANCE = new ExchangeRateDAO();

    private ExchangeRateDAO() {
    }

    public static ExchangeRateDAO getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public ExchangeRate save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        String sql = "INSERT INTO exchangeRates (BaseCurrencyID, TargetCurrencyID, Rate) VALUES (?, ?, ?)";

        Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            return null;
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
                        , resultSet.getBigDecimal("Rate"));

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
    public boolean update(int id, BigDecimal rate) {
        String sql = "UPDATE exchangeRates SET Rate=? WHERE ID=?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setInt(2, id);

            return preparedStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    @Override
    public ExchangeRate getRequiredExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            return null;
        }

        ExchangeRate exchangeRate = findByBaseAndTargetCurrencies(baseCurrency, targetCurrency);

        if (exchangeRate != null) {
            return exchangeRate;
        }

        exchangeRate = findByBaseAndTargetCurrencies(targetCurrency, baseCurrency);

        if (exchangeRate != null) {
            var goalRate = BigDecimal.valueOf(1).divide(exchangeRate.getRate()
                    , 6, RoundingMode.HALF_UP);

            return new ExchangeRate(baseCurrency, targetCurrency, goalRate);
        }

        List<ExchangeRate> exchangeRateList = findAll();
        ExchangeRate transExchangeRate = null;
        ExchangeRate goalExchangeRate = null;

        Set<ExchangeRate> exchangeRateSet = new LinkedHashSet<>();

        for (ExchangeRate exchangeRateFromList : exchangeRateList) {
            if (isThere(baseCurrency, exchangeRateFromList)) {
                exchangeRateSet.add(exchangeRateFromList);
            }
        }

        for (ExchangeRate exchangeRateFromSet : exchangeRateSet) {
            Currency transCurrency = getAnotherCurrency(baseCurrency, exchangeRateFromSet);

            for (ExchangeRate targetExchangeRate : exchangeRateList) {
                if (isGoal(targetCurrency, transCurrency, targetExchangeRate)) {
                    goalExchangeRate = targetExchangeRate;
                    transExchangeRate = exchangeRateFromSet;
                    break;
                }
            }

            if (goalExchangeRate != null) {
                break;
            }
        }

        if (goalExchangeRate == null) {
            return null;
        }
        else {
            BigDecimal goalRate = getGoalRate(baseCurrency, targetCurrency, transExchangeRate, goalExchangeRate);
            exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, goalRate);

            return exchangeRate;
        }
    }

    @Override
    public ExchangeRate findByBaseAndTargetCurrenciesCodes(String baseCurrencyCode, String targetCurrencyCode) {
        Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            return null;
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
            throw new DatabaseException("Couldn't to connect to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }

    private static BigDecimal getGoalRate(Currency baseCurrency, Currency targetCurrency
            , ExchangeRate transExchangeRate, ExchangeRate goalExchangeRate) {
        BigDecimal goalRate;

        if (baseCurrency.getId() == transExchangeRate.getBaseCurrency().getId()) {
            goalRate = transExchangeRate.getRate();
        }
        else {
            goalRate = BigDecimal.valueOf(1).divide(transExchangeRate.getRate()
                    , 6, RoundingMode.HALF_UP);
        }

        if (targetCurrency.getId() == goalExchangeRate.getTargetCurrency().getId()) {
            goalRate = goalRate.multiply(goalExchangeRate.getRate());
            goalRate = goalRate.setScale(6, RoundingMode.HALF_UP);
        }
        else {
            goalRate = goalRate.divide(goalExchangeRate.getRate(), 6, RoundingMode.HALF_UP);
        }

        return goalRate;
    }

    private static boolean isThere(Currency baseCurrency, ExchangeRate exchangeRate) {
        return baseCurrency.getId() == exchangeRate.getBaseCurrency().getId() || baseCurrency.getId() == exchangeRate.getTargetCurrency().getId();
    }

    private static boolean isGoal(Currency baseCurrency, Currency targetCurrency, ExchangeRate exchangeRate) {
        return isThere(baseCurrency, exchangeRate) && isThere(targetCurrency, exchangeRate);
    }

    private static Currency getAnotherCurrency(Currency currency, ExchangeRate exchangeRate) {
        if (currency.getId() != exchangeRate.getBaseCurrency().getId()) {
            return exchangeRate.getBaseCurrency();
        }
        else {

            return exchangeRate.getTargetCurrency();
        }
    }
}
