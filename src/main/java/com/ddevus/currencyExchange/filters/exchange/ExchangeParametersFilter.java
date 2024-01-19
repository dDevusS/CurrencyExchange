package com.ddevus.currencyExchange.filters.exchange;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.ddevus.currencyExchange.utils.FiltersUtil.*;

@WebFilter("/exchange")
public class ExchangeParametersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT
        var res = (HttpServletResponse) response;
        String baseCurrencyCode = request.getParameter("from");
        String targetCurrencyCode = request.getParameter("to");
        String amount = request.getParameter("amount");

        try {
            checkNumberFormat(amount);
            checkParameters(baseCurrencyCode, targetCurrencyCode);
        }
        catch (IncorrectParametersException exception) {
            handleException(res, exception);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
