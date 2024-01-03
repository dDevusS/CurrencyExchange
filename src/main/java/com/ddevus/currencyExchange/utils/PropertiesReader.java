package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.WrapperException;

import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
    private final static Properties PROPERTIES = new Properties();

    private PropertiesReader() {}

    static {
        loadPropertiesFile();
    }

    public static String read(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadPropertiesFile() {
        try (var inputStream =
                PropertiesReader.class.getClassLoader()
                        .getResourceAsStream("application.properties")) {
                PROPERTIES.load(inputStream);
        }
        catch (IOException e) {
            throw new DatabaseException("File application.properties was not found in resources root directory."
                    , WrapperException.ErrorReason.FAILED_READ_PROPERTIES
                    , e);
        }
    }
}
