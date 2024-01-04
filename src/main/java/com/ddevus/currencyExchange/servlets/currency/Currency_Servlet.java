package com.ddevus.currencyExchange.servlets.currency;

import com.ddevus.currencyExchange.services.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/currency/*")
public class Currency_Servlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(Currency_Servlet.class.getName());
    private final CurrencyService currencyService = Currency_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's GET request.");
        String servletPath = req.getPathInfo();
        String[] pathParts;
        pathParts = servletPath.split("/");

        var currency = currencyService.findByCode(pathParts[1]);
        logger.info("JSON Response: " + currency);

        try (var writer = resp.getWriter()) {
            writer.write(currency.toString());
        }
        logger.info("Finished processing GET request.");
    }
}
