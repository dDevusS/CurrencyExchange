package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.exceptions.WrapperException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CoreCurrencyExchangeServlet extends HttpServlet {

    protected void handleException(HttpServletResponse response, WrapperException e) {
        try (var writer = response.getWriter()) {
            response.setStatus(e.getSTATUS_CODE_HTTP_RESPONSE());
            writer.write(e.toString());
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
