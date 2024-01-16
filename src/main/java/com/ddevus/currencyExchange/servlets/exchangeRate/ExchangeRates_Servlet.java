package com.ddevus.currencyExchange.servlets.exchangeRate;

import com.ddevus.currencyExchange.entity.ExchangeRate;
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
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/exchangeRates")
public class ExchangeRates_Servlet extends BasicServlet {

    private final Logger logger = Logger.getLogger(ExchangeRates_Servlet.class.getName());
    private final IExchangeRate_Service exchangeRateService = ExchangeRate_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's GET request.");

        List<ExchangeRate> exchangeRateList = exchangeRateService.findAll();

        doResponse(exchangeRateList, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's POST request.");

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        BigDecimal rate = new BigDecimal(req.getParameter("rate"));
        rate = rate.setScale(6, RoundingMode.HALF_UP);

        var newExchangeRate
                = exchangeRateService.save(baseCurrencyCode, targetCurrencyCode, rate);

        if (newExchangeRate == null) {
            var exception = new SQLBadRequestException("There is no exchange rate with those currencies codes."
                    , WrapperException.ErrorReason.FAILED_FIND_EXCHANGE_RATE_IN_DB);

            handleException(resp, exception);
        }

        doResponse(newExchangeRate, resp);
    }

}
