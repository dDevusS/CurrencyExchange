package com.ddevus.currencyExchange.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConvertor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String getJson(Object object) throws JsonProcessingException {

        return OBJECT_MAPPER.writeValueAsString(object);
    }
}
