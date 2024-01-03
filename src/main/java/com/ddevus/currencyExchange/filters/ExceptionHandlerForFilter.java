package com.ddevus.currencyExchange.filters;

import com.ddevus.currencyExchange.exceptions.WrapperException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ExceptionHandlerForFilter {

    public static void handleException(ServletResponse response, WrapperException exception) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(exception.getSTATUS_CODE_HTTP_RESPONSE());
        try (var writer = response.getWriter()) {
            writer.write(exception.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
