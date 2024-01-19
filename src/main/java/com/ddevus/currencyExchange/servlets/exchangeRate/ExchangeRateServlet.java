package com.ddevus.currencyExchange.servlets.exchangeRate;

import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.NoResultException;
import com.ddevus.currencyExchange.services.ExchangeRateService;
import com.ddevus.currencyExchange.services.interfaces.IExchangeRate_Service;
import com.ddevus.currencyExchange.servlets.BasicServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@WebServlet("/exchangeRate/*")
@Log
public class ExchangeRateServlet extends BasicServlet {

    private static final IExchangeRate_Service EXCHANGE_RATE_SERVICE = ExchangeRateService.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Processing the client's GET request.");

        var currenciesCodes = extractCurrenciesCodes(req.getPathInfo());

        ExchangeRate exchangeRate
                = EXCHANGE_RATE_SERVICE.findByBaseAndTargetCurrenciesCodes(currenciesCodes[0]
                , currenciesCodes[1]);

        if (exchangeRate == null) {
            throw new NoResultException("There is no exchange rate with those currencies codes.");
        }

        doResponse(exchangeRate, resp);
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
        log.info("Processing the client's PATCH request.");

        BigDecimal rate = new BigDecimal(req.getParameter("rate"));
        rate = rate.setScale(6, RoundingMode.HALF_UP);
        var currenciesCodes = extractCurrenciesCodes(req.getPathInfo());

        ExchangeRate exchangeRate
                = EXCHANGE_RATE_SERVICE.update(currenciesCodes[0]
                , currenciesCodes[1]
                , rate);

        if (exchangeRate == null) {
            throw new NoResultException("There is no exchange rate with those currencies codes.");
        }

        doResponse(exchangeRate, resp);
    }

    private static String[] extractCurrenciesCodes(String pathInfo) {
        String[] pathParts = pathInfo.split("/");
        String baseCurrencyCode = pathParts[1].substring(0, 3);
        String targetCurrencyCode = pathParts[1].substring(3);

        return new String[]{baseCurrencyCode, targetCurrencyCode};
    }
}
