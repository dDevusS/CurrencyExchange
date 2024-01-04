package com.ddevus.currencyExchange.servlets;

import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import com.ddevus.currencyExchange.services.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.ExchangeRateService;
import com.ddevus.currencyExchange.services.ExchangeRate_Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchange")
public class Exchange_Servlet extends HttpServlet {

    private final CurrencyService currencyService = Currency_Service.getINSTANCE();
    private final ExchangeRateService exchangeRateService = ExchangeRate_Service.getINSTANCE();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        float amount = Float.parseFloat(req.getParameter("amount"));



        //ExchangerService
    }
}
