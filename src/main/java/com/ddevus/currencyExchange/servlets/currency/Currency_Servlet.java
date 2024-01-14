package com.ddevus.currencyExchange.servlets.currency;

import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.ICurrency_Service;
import com.ddevus.currencyExchange.utils.JsonConvertor;
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
    private final ICurrency_Service currencyService = Currency_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's GET request.");
        String servletPath = req.getPathInfo();
        String[] pathParts = servletPath.split("/");

        var currency = currencyService.findByCode(pathParts[1]);
        String json = JsonConvertor.getJson(currency);
        logger.info("JSON Response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
        logger.info("Finished processing GET request.");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Processing the client's DELETE request.");
        String servletPath = req.getPathInfo();
        String[] pathParts = servletPath.split("/");
        String json = null;

        if (currencyService.deleteByCode(pathParts[1])) {
            json = JsonConvertor.getJson(pathParts[1]);
            logger.info("JSON Response: " + "{\"message\":\"currency with code " + json + " was deleted\"}");
        }
        else {
            throw new SQLBadRequestException("There is no currency wit this code in the database."
            , WrapperException.ErrorReason.FAILED_FIND_CURRENCY_IN_DB);
        }

        try (var writer = resp.getWriter()) {
            writer.write("{\"message\":\"currency with code " + json + " was deleted\"}");
        }
        logger.info("Finished processing Delete request.");
    }
}
