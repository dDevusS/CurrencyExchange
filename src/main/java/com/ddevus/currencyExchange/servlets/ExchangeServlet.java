package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.dto.ExchangeDTO;
import com.ddevus.currencyExchange.services.ExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@WebServlet("/exchange")
public class ExchangeServlet extends BasicServlet {

    private final ExchangeService exchangeService = ExchangeService.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Processing the client's GET request.");

        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        amount = amount.setScale(6, RoundingMode.HALF_UP);

        ExchangeDTO exchangeDTO = exchangeService.exchangeAmount(baseCurrencyCode, targetCurrencyCode, amount);

        doResponse(exchangeDTO, resp);
    }
}
