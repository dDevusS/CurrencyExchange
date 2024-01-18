package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.BasicApplicationException;
import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.math.BigDecimal;

@UtilityClass
public class FiltersUtil {

    public static void handleException(ServletResponse response
            , BasicApplicationException exception) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(exception.getHTTP_CODE_STATUS());
        String json = "{\"errorMessage\":\"" + exception.getErrorMessage() + "\"}";

        try (var writer = response.getWriter()) {
            writer.write(json);
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
            throw new IncorrectParametersException("Required parameters are incorrect.");
        }
        catch (NullPointerException exception) {
            throw new IncorrectParametersException("Required parameter is missed.");
        }
    }

    public static void checkSentCodeParameters(String baseCurrencyCode, String targetCurrencyCode) {

        if (baseCurrencyCode == null || targetCurrencyCode == null) {
            throw new IncorrectParametersException("Required parameter is missed.");
        }
        else if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
            throw new IncorrectParametersException("Required parameters are incorrect.");
        }
    }
}
