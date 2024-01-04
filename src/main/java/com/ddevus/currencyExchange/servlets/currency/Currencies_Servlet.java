package com.ddevus.currencyExchange.servlets.currency;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.services.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class Currencies_Servlet extends HttpServlet {

    private final CurrencyService currencyService = Currency_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Currency> currencies = null;
             currencies = currencyService.findAll();

        var json = convertListToJson(currencies);
        System.out.println("JSON Response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sing = req.getParameter("sing");
        var newCurrency = new Currency( name, code, sing);

            newCurrency = currencyService.save(newCurrency);
        System.out.println("JSON Response: " + newCurrency);

        try (var writer = resp.getWriter()) {
            writer.write(newCurrency.toString());
        }
    }

    private String convertListToJson(List<Currency> currencies) {
        StringBuilder json = new StringBuilder("[");

        for (Currency currency : currencies) {
            json.append(currency).append(",");
        }

        if (json.length() > 1) {
            json.setCharAt(json.length() - 1, ']');
        }
        else {
            json.append("]");
        }

        return json.toString();
    }
}
