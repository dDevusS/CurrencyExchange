package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.WrapperException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final static String URL_KEY = "db.url";

    private ConnectionManager() {}

    public static Connection open() {
        try {
            System.out.println("Trying to find JDBC driver.");
            Class.forName("org.sqlite.JDBC");
            System.out.println("Finding JDBC driver is successful.");

            System.out.println("Trying to connect to the database.");
            var connection = DriverManager.getConnection(PropertiesReader.read(URL_KEY));
            System.out.println("Connecting to the database is successful.");

            return connection;
        }
        catch (ClassNotFoundException e) {
            throw new DatabaseException("Database driver was not found."
                    , WrapperException.ErrorReason.FAILED_FIND_JDBC_DRIVER);
        }
        catch (SQLException e) {
            throw new DatabaseException("Error connecting to the database."
                    , WrapperException.ErrorReason.UNKNOWN_ERROR_CONNECTING_TO_DB);
        }
    }
}