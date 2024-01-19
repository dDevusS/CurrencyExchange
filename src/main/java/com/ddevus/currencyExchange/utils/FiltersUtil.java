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
    private final static Logger LOG_EXCEPTION = Logger.getLogger(FiltersUtil.class.getName());

    public static void handleException(ServletResponse response
            , BasicApplicationException exception) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(exception.getHTTP_CODE_STATUS());

        String json = "{\"errorMessage\":\"" + exception.getErrorMessage() + "\"}";
        LOG_EXCEPTION.warning(json);

        try (var writer = response.getWriter()) {
            writer.write(json);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isCorrectCodePairParameter(String pathInfo) {
        String[] pathParts = pathInfo.split("/");

        return (pathParts.length == NUM_PARTS_PATH && pathParts[1].length() == CODE_PAIR_LENGTH);
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
        else if (baseCurrencyCode.length() != CODE_LENGTH || targetCurrencyCode.length() != CODE_LENGTH) {
            throw new IncorrectParametersException("Required parameters are incorrect.");
        }
    }
}
