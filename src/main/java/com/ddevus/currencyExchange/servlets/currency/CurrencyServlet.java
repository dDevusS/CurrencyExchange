package com.ddevus.currencyExchange.servlets.currency;

import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import com.ddevus.currencyExchange.services.CurrencyServiceImplementation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyServiceImplementation.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getPathInfo();
        String[] pathParts;
        String currencyCode;

        if (servletPath == null) {
            return;
        }

        pathParts = servletPath.split("/");

        if (pathParts.length != 2 || pathParts[1].length() != 3) {
            return;
        }

        currencyCode = pathParts[1];

        var currency = currencyService.findByCode(currencyCode);
        System.out.println("JSON Response: " + currency);

        try (var writer = resp.getWriter()) {
            writer.write(currency.toString());
        }
    }
}
