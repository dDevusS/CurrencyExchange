package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.DatabaseException;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class PropertiesReader {

    private final static Properties PROPERTIES = new Properties();
    private final static String PROPERTIES_PATH = "application.properties";

    static {
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
            throw new DatabaseException("File application.properties was not found in resources root directory.");
        }
    }
}