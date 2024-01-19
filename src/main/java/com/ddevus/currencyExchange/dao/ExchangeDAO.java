package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.dao.interfaces.IExchangeDAO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.NoResultException;
import com.ddevus.currencyExchange.utils.ConnectionManager;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExchangeDAO implements IExchangeDAO {

    private static final CurrencyDAO CURRENCY_DAO = CurrencyDAO.getINSTANCE();
    private static final ExchangeRateDAO EXCHANGE_RATE_DAO = ExchangeRateDAO.getINSTANCE();
    @Getter
    private static final ExchangeDAO INSTANCE = new ExchangeDAO();

    private ExchangeDAO() {
    }

    public ExchangeRate getRequiredExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        Currency baseCurrency = CURRENCY_DAO.findByCode(baseCurrencyCode);
        Currency targetCurrency = CURRENCY_DAO.findByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new NoResultException("There are no currencies with those codes in the database.");
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

        return findCrossedExchangeRate(baseCurrency, targetCurrency);
    }

    private ExchangeRate findCrossedExchangeRate(Currency baseCurrency, Currency targetCurrency) {
        List<ExchangeRate> exchangeRateList = EXCHANGE_RATE_DAO.findAll();
        Set<ExchangeRate> exchangeRateSet = new LinkedHashSet<>();

        fillSetWithThisCurrencyFromList(exchangeRateSet, baseCurrency, exchangeRateList);

        ExchangeRate[] goalAndTransExchangeRates
                = findGoalAndTransExchangeRates(baseCurrency, targetCurrency, exchangeRateSet, exchangeRateList);

        if (goalAndTransExchangeRates[0] == null || goalAndTransExchangeRates[1] == null) {
            return null;
        }
        else {
            BigDecimal goalRate = getGoalRate(baseCurrency, targetCurrency
                    , goalAndTransExchangeRates[1], goalAndTransExchangeRates[0]);

            return new ExchangeRate(baseCurrency, targetCurrency, goalRate);
        }
    }

    private ExchangeRate[] findGoalAndTransExchangeRates(Currency baseCurrency, Currency targetCurrency
            , Set<ExchangeRate> exchangeRateSet, List<ExchangeRate> exchangeRateList) {
        ExchangeRate transExchangeRate = null;
        ExchangeRate goalExchangeRate = null;

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

        return new ExchangeRate[]{goalExchangeRate, transExchangeRate};
    }

    private void fillSetWithThisCurrencyFromList(Set<ExchangeRate> exchangeRateSet
            , Currency currency, List<ExchangeRate> exchangeRateList) {
        for (ExchangeRate exchangeRateFromList : exchangeRateList) {
            if (isThere(currency, exchangeRateFromList)) {
                exchangeRateSet.add(exchangeRateFromList);
            }
        }
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
