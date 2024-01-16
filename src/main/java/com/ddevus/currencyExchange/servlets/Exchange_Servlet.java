package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.services.Exchange_Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@WebServlet("/exchange")
public class Exchange_Servlet extends BasicServlet {

    private final Logger logger = LoggerFactory.getLogger(Exchange_Servlet.class.getName());
    private final Exchange_Service exchangeService = Exchange_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Processing the client's GET request.");
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        amount = amount.setScale(6, RoundingMode.HALF_UP);

        var exchangeDTO
                = exchangeService.exchangeAmount(baseCurrencyCode, targetCurrencyCode, amount);

        String json = getJson(exchangeDTO);
        logger.info("JSON Response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
        logger.info("Finished processing GET request.");
    }
}
