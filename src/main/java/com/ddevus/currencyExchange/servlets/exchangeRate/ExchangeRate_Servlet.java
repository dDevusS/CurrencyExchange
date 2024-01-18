package com.ddevus.currencyExchange.servlets.exchangeRate;

import com.ddevus.currencyExchange.entity.ExchangeRate;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.ExchangeRate_Service;
import com.ddevus.currencyExchange.services.interfaces.IExchangeRate_Service;
import com.ddevus.currencyExchange.servlets.BasicServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

@WebServlet("/exchangeRate/*")
public class ExchangeRate_Servlet extends BasicServlet {

    private final Logger logger = Logger.getLogger(ExchangeRate_Servlet.class.getName());
    private final IExchangeRate_Service exchangeRateService = ExchangeRate_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's GET request.");

        var currenciesCodes = extractCurrenciesCodes(req.getPathInfo());

        ExchangeRate exchangeRate = null;
        try {
            exchangeRate
                    = exchangeRateService.findByBaseAndTargetCurrenciesCodes(currenciesCodes[0]
                    , currenciesCodes[1]);
        }
        catch (SQLBadRequestException | DatabaseException e) {
            handleException(resp, e);
        }

        if (exchangeRate == null) {
            var exception = new SQLBadRequestException("There is no exchange rate with those currencies codes."
                    , WrapperException.ErrorReason.FAILED_FIND_EXCHANGE_RATE_IN_DB);

            handleException(resp, exception);
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
        logger.info("Processing the client's PATCH request.");

        BigDecimal rate = new BigDecimal(req.getParameter("rate"));
        rate = rate.setScale(6, RoundingMode.HALF_UP);
        var currenciesCodes = extractCurrenciesCodes(req.getPathInfo());

        ExchangeRate exchangeRate = null;
        try {
            exchangeRate
                    = exchangeRateService.update(currenciesCodes[0]
                    , currenciesCodes[1]
                    , rate);
        }
        catch (SQLBadRequestException | DatabaseException e) {
            handleException(resp, e);
        }

        if (exchangeRate == null) {
            var exception = new SQLBadRequestException("There is no exchange rate with those currencies codes."
                    , WrapperException.ErrorReason.FAILED_FIND_EXCHANGE_RATE_IN_DB);

            handleException(resp, exception);
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
