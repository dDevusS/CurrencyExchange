package com.ddevus.currencyExchange.filters.exchangeRate;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.utils.ExceptionHandlerForFilterUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter ("/exchangeRatesYYY")
public class ExchangeRates_Filter_ForCheckingRequestParameters implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = ((HttpServletRequest) request);
        var res = ((HttpServletResponse) response);
        var pathInfo = req.getPathInfo();

        if (("POST").equals(req.getMethod())) {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");

            try {
                float rate = Float.parseFloat(req.getParameter("rate"));
            }
            catch (NullPointerException exception) {
                var e = new IncorrectParametersException("Required parameter is missed."
                        , WrapperException.ErrorReason.INCORRECT_PARAMETERS);

                ExceptionHandlerForFilterUtil.handleException(res, e);
            }
            catch (NumberFormatException exception) {
                var e = new IncorrectParametersException("Required parameters are incorrect."
                        , WrapperException.ErrorReason.INCORRECT_PARAMETERS);

                ExceptionHandlerForFilterUtil.handleException(res, e);
            }

            if (baseCurrencyCode == null || targetCurrencyCode == null) {
                var e = new IncorrectParametersException("Required parameter is missed."
                        , WrapperException.ErrorReason.INCORRECT_PARAMETERS);

                ExceptionHandlerForFilterUtil.handleException(res, e);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
