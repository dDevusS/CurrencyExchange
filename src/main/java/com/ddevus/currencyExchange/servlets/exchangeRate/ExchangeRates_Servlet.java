package com.ddevus.currencyExchange.servlets.exchangeRate;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
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
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRates_Servlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRate_Service.getINSTANCE();
    private final CurrencyService currencyService = Currency_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<ExchangeRate> exchangeRateList = exchangeRateService.findAll();

        var json = convertListToJson(exchangeRateList);
        System.out.println("JSON Response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        float rate = Float.parseFloat(req.getParameter("rate"));

        Currency baseCurrency = currencyService.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.findByCode(targetCurrencyCode);

        var newExchangeRate = new ExchangeRate( baseCurrency, targetCurrency, rate);
        newExchangeRate = exchangeRateService.save(newExchangeRate);
        System.out.println("JSON Response: " + newExchangeRate);

        try (var writer = resp.getWriter()) {
            writer.write(newExchangeRate.toString());
        }
    }

    private String convertListToJson(List<ExchangeRate> exchangeRateList) {
        StringBuilder json = new StringBuilder("[");

        for (ExchangeRate exchangeRate : exchangeRateList) {
            json.append(exchangeRate).append(",");
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
