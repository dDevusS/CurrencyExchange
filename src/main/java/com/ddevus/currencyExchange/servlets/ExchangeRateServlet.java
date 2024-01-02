package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.services.CurrencyService;
import com.ddevus.currencyExchange.services.CurrencyServiceImplementation;
import com.ddevus.currencyExchange.services.ExchangeRateService;
import com.ddevus.currencyExchange.services.ExchangeRateServiceImplementation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImplementation.getINSTANCE();
    private final CurrencyService currencyService = CurrencyServiceImplementation.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getPathInfo();
        String[] pathParts;
        String currenciesCode;

        if (servletPath == null) {
            return;
        }

        pathParts = servletPath.split("/");

        if (pathParts.length != 2 & pathParts[1].length() != 6) {
            return;
        }

        String baseCurrencyCode = pathParts[1].substring(0, 3);
        String targetCurrencyCode = pathParts[1].substring(3);

        var exchangeRate
                = exchangeRateService.findByBaseAndTargetCurrenciesCode(baseCurrencyCode, targetCurrencyCode);

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
        System.out.println("At patch method");
    }
}
