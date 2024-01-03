package com.ddevus.currencyExchange.servlets.exchangeRate;

import com.ddevus.currencyExchange.dto.CurrencyDTO;
import com.ddevus.currencyExchange.dto.ExchangeRateDTO;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import com.ddevus.currencyExchange.services.CurrencyServiceImplementation;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRateService;
import com.ddevus.currencyExchange.services.ExchangeRateServiceImplementation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImplementation.getINSTANCE();
    private final CurrencyService currencyService = CurrencyServiceImplementation.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRateDTO> exchangeRateDTOList = exchangeRateService.findAll();
        var json = convertListToJson(exchangeRateDTOList);

        System.out.println("JSON Response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        float rate = Float.parseFloat(req.getParameter("rate"));

        CurrencyDTO baseCurrency = currencyService.findByCode(baseCurrencyCode);
        CurrencyDTO targetCurrency = currencyService.findByCode(targetCurrencyCode);


        var newExchangeRate = new ExchangeRateDTO(0, baseCurrency, targetCurrency, rate);

        System.out.println("JSON Response: " + newExchangeRate);

        try (var writer = resp.getWriter()) {
            writer.write(newExchangeRate.toString());
        }
    }

    private String convertListToJson(List<ExchangeRateDTO> exchangeRateDTOList) {
        StringBuilder json = new StringBuilder("[");

        for (ExchangeRateDTO exchangeRateDTO : exchangeRateDTOList) {
            json.append(exchangeRateDTO).append(",");
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
