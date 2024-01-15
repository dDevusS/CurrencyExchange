package com.ddevus.currencyExchange.servlets.exchangeRate;

import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.ExchangeRate_Service;
import com.ddevus.currencyExchange.services.interfaces.IExchangeRate_Service;
import com.ddevus.currencyExchange.servlets.BasicServlet;
import com.ddevus.currencyExchange.utils.JsonConvertor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@WebServlet("/exchangeRate/*")
public class ExchangeRate_Servlet extends BasicServlet {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRate_Servlet.class.getName());
    private final IExchangeRate_Service exchangeRateService = ExchangeRate_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's GET request.");
        var currenciesCodes = extractCurrenciesCodes(req.getPathInfo());

            var exchangeRate
                    = exchangeRateService.findByBaseAndTargetCurrenciesCodes(currenciesCodes[0]
                    , currenciesCodes[1]);

        if (exchangeRate == null) {
            var exception = new SQLBadRequestException("There is no exchange rate with those currencies codes."
            , WrapperException.ErrorReason.FAILED_FIND_EXCHANGE_RATE_IN_DB);

            handleException(resp, exception);
        }

        String json = JsonConvertor.getJson(exchangeRate);
        logger.info("JSON Response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
        logger.info("Finished processing GET request.");
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

        var exchangeRate
                = exchangeRateService.update(currenciesCodes[0]
                , currenciesCodes[1]
                , rate);

        if (exchangeRate == null) {
            var exception = new SQLBadRequestException("There is no exchange rate with those currencies codes."
                    , WrapperException.ErrorReason.FAILED_FIND_EXCHANGE_RATE_IN_DB);

            handleException(resp, exception);
        }

        String json = JsonConvertor.getJson(exchangeRate);
        logger.info("JSON response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
        logger.info("Finished processing PATCH request.");
    }

    private static String[] extractCurrenciesCodes(String pathInfo) {
        String[] pathParts = pathInfo.split("/");
        String baseCurrencyCode = pathParts[1].substring(0, 3);
        String targetCurrencyCode = pathParts[1].substring(3);

        return new String[]{baseCurrencyCode, targetCurrencyCode};
    }
}
