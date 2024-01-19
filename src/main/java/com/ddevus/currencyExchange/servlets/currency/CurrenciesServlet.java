package com.ddevus.currencyExchange.servlets.currency;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.exceptions.InsertFailedException;
import com.ddevus.currencyExchange.services.CurrencyService;
import com.ddevus.currencyExchange.services.interfaces.ICurrency_Service;
import com.ddevus.currencyExchange.servlets.BasicServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
@Log
public class CurrenciesServlet extends BasicServlet {

    private static final ICurrency_Service CURRENCY_SERVICE = CurrencyService.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Processing the client's GET request.");

        List<Currency> currencies = CURRENCY_SERVICE.findAll();

        doResponse(currencies, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Processing the client's POST request.");

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        var newCurrency = new Currency(name, code, sign);

        newCurrency = CURRENCY_SERVICE.save(newCurrency);

        if (newCurrency == null) {
            throw new InsertFailedException("There is a currency in the database with the following parameters.");
        }

        doResponse(newCurrency, resp);
    }

}
