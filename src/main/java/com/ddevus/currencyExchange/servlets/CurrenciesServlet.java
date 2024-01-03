package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.dto.CurrencyDTO;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.services.CurrencyService;
import com.ddevus.currencyExchange.services.CurrencyServiceImplementation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends CoreCurrencyExchangeServlet {

    private final CurrencyService currencyService = CurrencyServiceImplementation.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<CurrencyDTO> currencies = null;
        try {
             currencies = currencyService.findAll();
        }
        catch (DatabaseException e) {
            resp.setStatus(e.getSTATUS_CODE_HTTP_RESPONSE());

            try (var writer = resp.getWriter()) {
                writer.write(e.toString());
            }
        }
            var json = convertListToJson(currencies);
            System.out.println("JSON Response: " + json);


        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, DatabaseException, SQLBadRequestException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sing = req.getParameter("sing");

        var newCurrency = new CurrencyDTO(0, name, code, sing);
        try {
            newCurrency = currencyService.save(newCurrency);
        }
        catch (DatabaseException e) {
            resp.setStatus(e.getSTATUS_CODE_HTTP_RESPONSE());

            try (var writer = resp.getWriter()) {
                writer.write(e.toString());
            }
        }
        catch (SQLBadRequestException e) {
            resp.setStatus(e.getSTATUS_CODE_HTTP_RESPONSE());

            try (var writer = resp.getWriter()) {
                writer.write(e.toString());
            }
        }

        System.out.println("JSON Response: " + newCurrency);

        try (var writer = resp.getWriter()) {
            writer.write(newCurrency.toString());
        }

    }

    private String convertListToJson(List<CurrencyDTO> currencies) {
        StringBuilder json = new StringBuilder("[");

        for (CurrencyDTO currency : currencies) {
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
