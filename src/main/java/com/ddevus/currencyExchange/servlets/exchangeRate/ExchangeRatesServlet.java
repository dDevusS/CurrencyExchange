package com.ddevus.currencyExchange.servlets.exchangeRate;

import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.InsertFailedException;
import com.ddevus.currencyExchange.services.ExchangeRateService;
import com.ddevus.currencyExchange.services.interfaces.IExchangeRate_Service;
import com.ddevus.currencyExchange.servlets.BasicServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BasicServlet {

    private static final IExchangeRate_Service EXCHANGE_RATE_SERVICE = ExchangeRateService.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOG_INFO.info("Processing the client's GET request.");

        List<ExchangeRate> exchangeRateList = EXCHANGE_RATE_SERVICE.findAll();

        doResponse(exchangeRateList, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOG_INFO.info("Processing the client's POST request.");

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        BigDecimal rate = new BigDecimal(req.getParameter("rate"));
        rate = rate.setScale(6, RoundingMode.HALF_UP);

        ExchangeRate newExchangeRate
                = EXCHANGE_RATE_SERVICE.save(baseCurrencyCode, targetCurrencyCode, rate);

        if (newExchangeRate == null) {
            throw new InsertFailedException("There is a exchange rate in the database with those currencies codes.");
        }

        doResponse(newExchangeRate, resp);
    }

}
