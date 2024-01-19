package com.ddevus.currencyExchange.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public abstract class BasicServlet extends HttpServlet {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String getJson(Object object) throws JsonProcessingException {

        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static void doResponse(Object object, HttpServletResponse resp) throws IOException {
        String json = getJson(object);
        log.info("JSON Response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }
}
