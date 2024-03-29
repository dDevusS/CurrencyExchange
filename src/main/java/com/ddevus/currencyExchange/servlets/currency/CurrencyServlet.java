package com.ddevus.currencyExchange.servlets.currency;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.exceptions.NoResultException;
import com.ddevus.currencyExchange.services.CurrencyService;
import com.ddevus.currencyExchange.services.interfaces.ICurrencyService;
import com.ddevus.currencyExchange.servlets.BasicServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;

@WebServlet("/currency/*")
@Log
public class CurrencyServlet extends BasicServlet {

    private static final ICurrencyService CURRENCY_SERVICE = CurrencyService.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Processing the client's GET request.");

        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");

        Currency currency = CURRENCY_SERVICE.findByCode(pathParts[1]);

        if (currency == null) {
            throw new NoResultException("There is no currency with this code in the database."
            , "Entered parameters: pathInfo: " + pathInfo + ".");
        }

        doResponse(currency, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Processing the client's DELETE request.");

        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");

        if (!CURRENCY_SERVICE.deleteByCode(pathParts[1])) {
            throw new NoResultException("There is no currency with this code in the database."
            , "Entered parameters: pathInfo: " + pathInfo + ".");
        }

        String json = "{\"message\":\"currency with code " + pathParts[1] + " was deleted\"}";
        log.info("Json response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }
}
