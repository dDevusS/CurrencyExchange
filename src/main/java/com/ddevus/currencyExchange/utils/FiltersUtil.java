package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

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

    public static boolean isCorrectCodePairParameter(String pathInfo) {
        String[] pathParts = pathInfo.split("/");

        return (pathParts.length == 2 && pathParts[1].length() == 6);
    }

    public static void checkNumberFormat(String number) {
        try {
            new BigDecimal(number);
        }
        catch (NumberFormatException exception) {
            throw new IncorrectParametersException("Required parameters are incorrect."
                    , WrapperException.ErrorReason.INCORRECT_PARAMETERS);
        }
        catch (NullPointerException exception) {
            throw new IncorrectParametersException("Required parameter is missed."
                    , WrapperException.ErrorReason.INCORRECT_PARAMETERS);
        }
    }

    public static void checkSentCodeParameters(String baseCurrencyCode, String targetCurrencyCode) {

        if (baseCurrencyCode == null || targetCurrencyCode == null) {
            throw new IncorrectParametersException("Required parameter is missed."
                    , WrapperException.ErrorReason.INCORRECT_PARAMETERS);
        }
        else if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
            throw new IncorrectParametersException("Required parameters are incorrect."
                    , WrapperException.ErrorReason.INCORRECT_PARAMETERS);
        }
    }
}
