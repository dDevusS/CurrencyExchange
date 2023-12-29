package com.ddevus.currencyExchange.utils;

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
            throw new RuntimeException(e);
        }
    }
}
