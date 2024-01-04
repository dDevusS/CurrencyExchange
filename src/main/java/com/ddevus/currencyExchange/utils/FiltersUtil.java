package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FiltersUtil {

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

    public static boolean isCorrectParameter(String pathInfo) {
        String[] pathParts = pathInfo.split("/");

        return (pathParts.length == 2 && pathParts[1].length() == 6);
    }

    public static void checkRateFormat(String rate, HttpServletResponse res) {
        try {
            Float.parseFloat(rate);
        }
        catch (NumberFormatException exception) {
            var e = new IncorrectParametersException("Required parameters are incorrect."
                    , WrapperException.ErrorReason.INCORRECT_PARAMETERS);

            FiltersUtil.handleException(res, e);
        }
        catch (NullPointerException exception) {
            var e = new IncorrectParametersException("Required parameter is missed."
                    , WrapperException.ErrorReason.INCORRECT_PARAMETERS);

            FiltersUtil.handleException(res, e);
        }
    }
}
