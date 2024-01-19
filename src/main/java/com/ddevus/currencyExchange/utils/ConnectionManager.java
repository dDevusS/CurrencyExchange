package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.DatabaseException;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

@UtilityClass
public class ConnectionManager {

    private final static String URL_KEY = "db.url";
    private static final Logger LOG_INFO = Logger.getLogger(ConnectionManager.class.getName());

    public static Connection open() {
        try {
            Class.forName("org.sqlite.JDBC");

            LOG_INFO.info("Connecting to the database.");

            return DriverManager.getConnection(PropertiesReader.read(URL_KEY));
        }
        catch (ClassNotFoundException e) {
            throw new DatabaseException("Database driver was not found.");
        }
        catch (SQLException e) {
            throw new DatabaseException("Error connecting to the database.");
        }
    }
}