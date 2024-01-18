package com.ddevus.currencyExchange.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

public abstract class BasicServlet extends HttpServlet {

    protected static final Logger logger = Logger.getLogger(BasicServlet.class.getName());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String getJson(Object object) throws JsonProcessingException {

        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static void doResponse(Object object, HttpServletResponse resp) throws IOException {
        String json = getJson(object);
        logger.info("JSON Response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }
}
