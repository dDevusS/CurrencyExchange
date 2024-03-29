package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.BasicApplicationException;
import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

import java.io.IOException;
import java.math.BigDecimal;

@UtilityClass
@Log
public class FiltersUtil {

    private final static int CODE_LENGTH = 3;
    private final static int CODE_PAIR_LENGTH = 6;
    private final static int NUM_PARTS_PATH = 2;
    private final static int SIGN_LENGTH = 3;

    public static void handleException(ServletResponse response
            , BasicApplicationException exception) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(exception.getHttpCodeStatus());

        String json = "{\"errorMessage\":\"" + exception.getErrorMessage() + "\"}";

        log.warning(exception.getErrorMessage() + " " + exception.getDetailsOfError());

        try (var writer = response.getWriter()) {
            writer.write(json);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkParameters(String name, String code, String sign) {
        if (name == null || code == null || sign == null) {
            throw new IncorrectParametersException("Required parameters are missing.");
        }
        else if (!isCorrectCode(code) || !isCorrectSign(sign)) {
            throw new IncorrectParametersException("Required parameters are incorrect.");
        }
    }

    public static void checkParameters(String baseCurrencyCode, String targetCurrencyCode) {

        if (baseCurrencyCode == null || targetCurrencyCode == null) {
            throw new IncorrectParametersException("Required parameter is missed.");
        }
        else if (!isCorrectCode(baseCurrencyCode) || !isCorrectCode(targetCurrencyCode)) {
            throw new IncorrectParametersException("Required parameters are incorrect.");
        }
    }

    public static void checkCurrencyPathCode(String pathInfo) {
        String[] pathParts = pathInfo.split("/");

        if (!isPathCode(pathParts) || !isCorrectCode(pathParts[1])) {
            throw new IncorrectParametersException("Required parameters are incorrect.");
        }
    }

    public static void checkExchangeRatePathCode(String pathInfo) {
        String[] pathParts = pathInfo.split("/");

        if (!isPathCode(pathParts) || pathParts[1].length() != CODE_PAIR_LENGTH) {
            throw new IncorrectParametersException("Required parameters are incorrect.");
        }
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

    private static boolean isCorrectCode(String code) {

        return code.length() == CODE_LENGTH;
    }

    private static boolean isCorrectSign(String sing) {

        return sing.length() == SIGN_LENGTH;
    }

    private static boolean isPathCode(String[] pathParts) {

        return pathParts.length == NUM_PARTS_PATH;
    }
}
