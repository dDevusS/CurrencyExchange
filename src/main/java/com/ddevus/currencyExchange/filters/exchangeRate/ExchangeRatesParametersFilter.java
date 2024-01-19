package com.ddevus.currencyExchange.filters.exchangeRate;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.ddevus.currencyExchange.utils.FiltersUtil.*;

@WebFilter("/exchangeRates")
public class ExchangeRatesParametersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = ((HttpServletRequest) request);
        var res = ((HttpServletResponse) response);

        if (("POST").equals(req.getMethod())) {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rate = req.getParameter("rate");

            try {
                checkNumberFormat(rate);
                checkParameters(baseCurrencyCode, targetCurrencyCode);
            }
            catch (IncorrectParametersException exception) {
                handleException(res, exception);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
