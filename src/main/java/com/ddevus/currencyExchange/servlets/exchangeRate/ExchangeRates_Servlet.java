package com.ddevus.currencyExchange.servlets.exchangeRate;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.services.interfaces.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRate_Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRates_Servlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRates_Servlet.class.getName());
    private final ExchangeRate_Service exchangeRateService = com.ddevus.currencyExchange.services.ExchangeRate_Service.getINSTANCE();
    private final Currency_Service currencyService = com.ddevus.currencyExchange.services.Currency_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's GET request.");
        List<ExchangeRate> exchangeRateList = exchangeRateService.findAll();

        var json = convertListToJson(exchangeRateList);
        logger.info("JSON Response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
        logger.info("Finished processing GET request.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's POST request.");
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        float rate = Float.parseFloat(req.getParameter("rate"));

        Currency baseCurrency = currencyService.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.findByCode(targetCurrencyCode);

        var newExchangeRate = new ExchangeRate( baseCurrency, targetCurrency, rate);
        newExchangeRate = exchangeRateService.save(newExchangeRate);
        logger.info("JSON Response: " + newExchangeRate);

        try (var writer = resp.getWriter()) {
            writer.write(newExchangeRate.toString());
        }
        logger.info("Finished processing POST request.");
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
