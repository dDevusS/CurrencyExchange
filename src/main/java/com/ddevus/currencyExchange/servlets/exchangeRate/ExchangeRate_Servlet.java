package com.ddevus.currencyExchange.servlets.exchangeRate;

import com.ddevus.currencyExchange.services.Currency_Service;
import com.ddevus.currencyExchange.services.ExchangeRate_Service;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRate_Servlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRate_Service.getINSTANCE();
    private final CurrencyService currencyService = Currency_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        var currenciesCodes = extractCurrenciesCodes(req.getPathInfo());

        var exchangeRate
                = exchangeRateService.findByBaseAndTargetCurrenciesCode(currenciesCodes[0]
                , currenciesCodes[1]);

        System.out.println("JSON Response: " + exchangeRate);

        try (var writer = resp.getWriter()) {
            writer.write(exchangeRate.toString());
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getMethod();

        if (!("PATCH").equals(method)) {
            super.service(req, resp);
        }

        this.doPatch(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        float rate = Float.parseFloat(req.getParameter("rate"));
        String servletPath = req.getPathInfo();
        var currenciesCodes = extractCurrenciesCodes(req.getPathInfo());

        var exchangeRate
                = exchangeRateService.update(currenciesCodes[0]
                , currenciesCodes[1]
                , rate);

        System.out.println("JSON response: " + exchangeRate);

        try (var writer = resp.getWriter()) {
            writer.write(exchangeRate.toString());
        }
    }

    private static String[] extractCurrenciesCodes(String pathInfo) {
        String[] pathParts = pathInfo.split("/");
        String baseCurrencyCode = pathParts[1].substring(0, 3);
        String targetCurrencyCode = pathParts[1].substring(3);

        return new String[]{ baseCurrencyCode, targetCurrencyCode};
    }
}
