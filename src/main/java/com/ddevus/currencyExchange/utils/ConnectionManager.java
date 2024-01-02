package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.DataBaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final static String URL_KEY = "db.url";

    private ConnectionManager() {}

    public static Connection open() throws DataBaseException {
        System.out.println("Trying to connect to DB");

        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(PropertiesReader.read(URL_KEY));
        }
        catch (ClassNotFoundException e) {
            throw new DataBaseException("Database driver was not found.", e);
        }
        catch (SQLException e) {
            throw new DataBaseException("Error connecting to the database.", e);
        }
    }
}
