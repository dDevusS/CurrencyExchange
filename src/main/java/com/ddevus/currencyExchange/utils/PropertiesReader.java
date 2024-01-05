package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.WrapperException;

import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private final static Properties PROPERTIES = new Properties();
    private final static String PROPERTIES_PATH = "application.properties";

    private PropertiesReader() {
    }

    static {
        //TODO: ?
        // Can't catch exception if PROPERTIES_PATH is wrong.

        loadPropertiesFile();
    }

    public static String read(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadPropertiesFile() {
        try (var inputStream =
                     PropertiesReader.class.getClassLoader()
                             .getResourceAsStream(PROPERTIES_PATH)) {
            PROPERTIES.load(inputStream);
        }
        catch (IOException e) {
            throw new DatabaseException("File application.properties was not found in resources root directory."
                    , WrapperException.ErrorReason.FAILED_READ_PROPERTIES);
        }
    }
}