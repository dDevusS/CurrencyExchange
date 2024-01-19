package com.ddevus.currencyExchange.dao;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.utils.ConnectionManager;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO implements com.ddevus.currencyExchange.dao.interfaces.ICurrencyDAO {

    @Getter
    private static final CurrencyDAO INSTANCE = new CurrencyDAO();

    private CurrencyDAO() {
    }

    @Override
    public Currency save(Currency currency) {
        String sql = "INSERT INTO currencies (Code, FullName, Sign) VALUES (?, ?, ?)";

        try (var connection = ConnectionManager.open();
             var preparedStatement
                     = connection.prepareStatement(sql, new String[]{"ID"})) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSign());

            try {
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                return null;
            }

            try (var statement = connection.createStatement();
                 var resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
                if (resultSet.next()) {
                    currency.setId(resultSet.getInt(1));
                    return currency;
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
    public Currency findById(int id) {
        String sql = "SELECT * FROM currencies WHERE ID = ?";

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Currency currency = createCurrency(resultSet);

                return Optional.of(currency).get();
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database.");
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

                return Optional.of(currency).get();
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database.");
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
            throw new DatabaseException("Couldn't to connect to the database.");
        }
    }

    @Override
    public boolean deleteByCode(String code) {
        String sql = "DELETE FROM currencies WHERE Code = ?";

        try (var connection = ConnectionManager.open();
             var preparedStatement
                     = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, code);

            return preparedStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't to connect to the database.");
        }
    }

    private static Currency createCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("ID"),
                resultSet.getString("Code"),
                resultSet.getString("FullName"),
                resultSet.getString("Sign")
        );
    }
}
