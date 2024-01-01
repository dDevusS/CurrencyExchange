package com.ddevus.currencyExchange.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final static String URL_KEY = "db.url";

    private ConnectionManager() {}

    public static Connection open() {
        System.out.println("Trying to connect to DB");

        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(PropertiesReader.read(URL_KEY));
        }
        catch (SQLException | ClassNotFoundException e) {
            System.err.println("Couldn't to connect to DB");
            throw new RuntimeException(e);
        }
    }
}
