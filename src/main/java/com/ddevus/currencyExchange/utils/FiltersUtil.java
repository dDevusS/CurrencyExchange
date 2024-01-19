package com.ddevus.currencyExchange.utils;

import com.ddevus.currencyExchange.exceptions.BasicApplicationException;
import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

@UtilityClass
public class FiltersUtil {

    private final static int CODE_LENGTH = 3;
    private final static int CODE_PAIR_LENGTH = 6;
    private final static int NUM_PARTS_PATH = 2;
    private final static int SIGN_LENGTH = 3;
    private final static Logger LOG_EXCEPTION = Logger.getLogger(FiltersUtil.class.getName());

    public static void handleException(ServletResponse response
            , BasicApplicationException exception) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(exception.getHttpCodeStatus());

        String json = "{\"errorMessage\":\"" + exception.getErrorMessage() + "\"}";
        LOG_EXCEPTION.warning(json);

        try (var writer = response.getWriter()) {
            writer.write(json);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkCurrencyParameters(String name, String code, String sign) {
        if (name == null || code == null || sign == null) {
            throw new IncorrectParametersException("Required parameters are missing.");
        }
        else if (!isCorrectCode(code) || !isCorrectSign(sign)) {
           throw new IncorrectParametersException("Required parameters are incorrect.");
        }
    }

    public static void checkCurrencyPathCode(String[] pathParts) {
        if (!isPathCode(pathParts) || !isCorrectCode(pathParts[1])) {
            throw new IncorrectParametersException("Required parameters are incorrect.");
        }
    }

    private static boolean isCorrectCode (String code) {

        return code.length() == CODE_LENGTH;
    }

    private static boolean isCorrectSign (String sing) {

        return sing.length() == SIGN_LENGTH;
    }

    private static boolean isPathCode(String[] pathParts) {

        return pathParts.length == NUM_PARTS_PATH;
    }

    public static boolean isCorrectCodePairParameter(String pathInfo) {
        String[] pathParts = pathInfo.split("/");

        return (isPathCode(pathParts) && pathParts[1].length() == CODE_PAIR_LENGTH);
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
        else if (isCorrectCode(baseCurrencyCode) || isCorrectCode(targetCurrencyCode)) {
            throw new IncorrectParametersException("Required parameters are incorrect.");
        }
    }
}
