package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.WrapperException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConnectionManager {

    private final static String URL_KEY = "db.url";
    private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            logger.info("Finding JDBC driver.");
            Class.forName("org.sqlite.JDBC");

            logger.info("Connecting to the database.");

            return DriverManager.getConnection(PropertiesReader.read(URL_KEY));
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