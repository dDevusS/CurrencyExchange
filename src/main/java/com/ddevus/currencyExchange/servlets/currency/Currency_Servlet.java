package com.ddevus.currencyExchange.servlets.currency;

import com.ddevus.currencyExchange.services.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class Currency_Servlet extends HttpServlet {

    private final CurrencyService currencyService = Currency_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String servletPath = req.getPathInfo();
        String[] pathParts;
        pathParts = servletPath.split("/");

        var currency = currencyService.findByCode(pathParts[1]);
        System.out.println("JSON Response: " + currency);

        try (var writer = resp.getWriter()) {
            writer.write(currency.toString());
        }
    }
}
