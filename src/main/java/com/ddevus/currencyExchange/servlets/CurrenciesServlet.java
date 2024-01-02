package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.dto.CurrencyDTO;
import com.ddevus.currencyExchange.exceptions.DataBaseException;
import com.ddevus.currencyExchange.services.CurrencyService;
import com.ddevus.currencyExchange.services.CurrencyServiceImplementation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyServiceImplementation.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        List<CurrencyDTO> currencies = null;
        try {
             currencies = currencyService.findAll();
        }
        catch (DataBaseException e) {
            resp.setStatus(e.getSTATUS_CODE_HTTP_RESPONSE());

            try (var writer = resp.getWriter()) {
                writer.write(e.toString());
            }
            return;
        }
            var json = convertListToJson(currencies);
            System.out.println("JSON Response: " + json);


        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sing = req.getParameter("sing");

        var newCurrency = new CurrencyDTO(0, name, code, sing);
        newCurrency = currencyService.save(newCurrency);

        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

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
