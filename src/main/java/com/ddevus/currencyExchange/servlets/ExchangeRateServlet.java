package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.services.CurrencyService;
import com.ddevus.currencyExchange.services.CurrencyServiceImplementation;
import com.ddevus.currencyExchange.services.ExchangeRateService;
import com.ddevus.currencyExchange.services.ExchangeRateServiceImplementation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImplementation.getINSTANCE();
    private final CurrencyService currencyService = CurrencyServiceImplementation.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var currenciesCodes = extractCurrenciesCodes(req.getPathInfo());

        var exchangeRate
                = exchangeRateService.findByBaseAndTargetCurrenciesCode(currenciesCodes[0]
                , currenciesCodes[1]);

        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        System.out.println("JSON Response: " + exchangeRate);

        try (var writer = resp.getWriter()) {
            writer.write(exchangeRate.toString());
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();

        if (!method.equals("PATCH")) {
            super.service(req, resp);
        }

        this.doPatch(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        float rate = Float.parseFloat(req.getParameter("rate"));
        String servletPath = req.getPathInfo();
        var currenciesCodes = extractCurrenciesCodes(req.getPathInfo());

        var exchangeRate
                = exchangeRateService.update(currenciesCodes[0]
                , currenciesCodes[1]
                , rate);

        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        System.out.println("JSON response: " + exchangeRate);

        try (var writer = resp.getWriter()) {
            writer.write(exchangeRate.toString());
        }
    }

    private static String[] extractCurrenciesCodes(String pathInfo) {
        String[] pathParts;

        if (pathInfo == null) {
            return null;
        }

        pathParts = pathInfo.split("/");

        if (pathParts.length != 2 & pathParts[1].length() != 6) {
            return null;
        }

        String baseCurrencyCode = pathParts[1].substring(0, 3);
        String targetCurrencyCode = pathParts[1].substring(3);

        String[] baseAndTargetCurrenciesCodes = new String[]{ baseCurrencyCode, targetCurrencyCode};

        return baseAndTargetCurrenciesCodes;
    }
}
