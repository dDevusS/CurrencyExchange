package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.services.CurrencyService;
import com.ddevus.currencyExchange.services.CurrencyServiceImplementation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        System.out.println("JSON Response: " + currency);

        try (var writer = resp.getWriter()) {
            writer.write(currency.toString());
        }
    }
}
