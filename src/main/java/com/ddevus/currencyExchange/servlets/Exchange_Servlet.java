package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.services.Exchange_Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class Exchange_Servlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(Exchange_Servlet.class.getName());
    private final Exchange_Service exchangeService = Exchange_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Processing the client's GET request.");
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));

        var exchangeDTO
                = exchangeService.exchangeAmount(baseCurrencyCode, targetCurrencyCode, amount);
        logger.info("JSON Response: " + exchangeDTO);

        try (var writer = resp.getWriter()) {
            writer.write(exchangeDTO.toString());
        }
        logger.info("Finished processing GET request.");
    }
}
