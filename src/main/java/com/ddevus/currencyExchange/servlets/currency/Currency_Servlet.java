package com.ddevus.currencyExchange.servlets.currency;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.exceptions.NoResultException;
import com.ddevus.currencyExchange.services.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.ICurrency_Service;
import com.ddevus.currencyExchange.servlets.BasicServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class Currency_Servlet extends BasicServlet {

    private final ICurrency_Service currencyService = Currency_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's GET request.");

        String servletPath = req.getPathInfo();
        String[] pathParts = servletPath.split("/");

        Currency currency = currencyService.findByCode(pathParts[1]);

        if (currency == null) {
            throw new NoResultException("There is no currency with this code in the database.");
        }

        doResponse(currency, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Processing the client's DELETE request.");

        String servletPath = req.getPathInfo();
        String[] pathParts = servletPath.split("/");

        if (!currencyService.deleteByCode(pathParts[1])) {
            throw new NoResultException("There is no currency with this code in the database.");
        }

        String json = "{\"message\":\"currency with code " + pathParts[1] + " was deleted\"}";
        logger.info("Json response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }
}
