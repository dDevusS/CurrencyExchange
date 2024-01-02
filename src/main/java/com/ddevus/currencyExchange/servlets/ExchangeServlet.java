package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.services.CurrencyService;
import com.ddevus.currencyExchange.services.CurrencyServiceImplementation;
import com.ddevus.currencyExchange.services.ExchangeRateService;
import com.ddevus.currencyExchange.services.ExchangeRateServiceImplementation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchange/*")
public class ExchangeServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyServiceImplementation.getINSTANCE();
    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImplementation.getINSTANCE();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        float amount = Float.parseFloat(req.getParameter("amount"));



        //ExchangerService
    }
}