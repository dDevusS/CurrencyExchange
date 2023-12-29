package com.ddevus.currencyExchange.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final static String URL_KEY = "db.url";

    private ConnectionManager() {}

    public static Connection open() {
        try {
            return DriverManager.getConnection(PropertiesReader.read(URL_KEY));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
