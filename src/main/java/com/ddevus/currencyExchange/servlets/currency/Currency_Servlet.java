package com.ddevus.currencyExchange.servlets.currency;

import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.Currency_Service;
import com.ddevus.currencyExchange.services.interfaces.ICurrency_Service;
import com.ddevus.currencyExchange.servlets.BasicServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/currency/*")
public class Currency_Servlet extends BasicServlet {

    private final Logger logger = Logger.getLogger(Currency_Servlet.class.getName());
    private final ICurrency_Service currencyService = Currency_Service.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Processing the client's GET request.");

        String servletPath = req.getPathInfo();
        String[] pathParts = servletPath.split("/");

        Currency currency = null;
        try {
            currency = currencyService.findByCode(pathParts[1]);
        }
        catch (DatabaseException e) {
            handleException(resp, e);
        }

        if (currency == null) {
            var exception = new SQLBadRequestException("There is no currency with this code in the database."
                    , WrapperException.ErrorReason.FAILED_FIND_CURRENCY_IN_DB);

            handleException(resp, exception);
        }

        doResponse(currency, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Processing the client's DELETE request.");

        String servletPath = req.getPathInfo();
        String[] pathParts = servletPath.split("/");

        boolean isDeleted = false;
        try {
            isDeleted = currencyService.deleteByCode(pathParts[1]);
        }
        catch (DatabaseException e) {
            handleException(resp, e);
        }

        if (!isDeleted) {
            var exception = new SQLBadRequestException("There is no currency with this code in the database."
                    , WrapperException.ErrorReason.FAILED_FIND_CURRENCY_IN_DB);

            handleException(resp, exception);
        }

        String json = "{\"message\":\"currency with code " + pathParts[1] + " was deleted\"}";
        logger.info("Json response: " + json);

        try (var writer = resp.getWriter()) {
            writer.write(json);
        }
    }
}
