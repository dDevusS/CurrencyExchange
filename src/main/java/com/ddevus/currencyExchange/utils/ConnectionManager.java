package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.DatabaseException;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
@Log
public class ConnectionManager {

    private final static String URL_KEY = "db.url";

    public static Connection open() {
        try {
            Class.forName("org.sqlite.JDBC");

            log.info("Connecting to the database.");

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