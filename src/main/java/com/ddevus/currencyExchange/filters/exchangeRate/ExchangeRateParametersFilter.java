package com.ddevus.currencyExchange.filters.exchangeRate;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import com.ddevus.currencyExchange.utils.FiltersUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/exchangeRate/*")
public class ExchangeRateParametersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var req = ((HttpServletRequest) request);
        var res = ((HttpServletResponse) response);
        var pathInfo = req.getPathInfo();
        String rate = req.getParameter("rate");

        if (("GET").equals(req.getMethod())) {
            if (FiltersUtil.isCorrectCodePairParameter(pathInfo)) {
                chain.doFilter(request, response);
            }
            else {
                var exception = new IncorrectParametersException("Required parameters are incorrect.");
                FiltersUtil.handleException(res, exception);
            }
        }

        if (("PATCH").equals(req.getMethod())) {
            if (!FiltersUtil.isCorrectCodePairParameter(pathInfo)) {
                var exception
                        = new IncorrectParametersException("Required parameters are incorrect.");

                FiltersUtil.handleException(res, exception);
            }

            try {
                FiltersUtil.checkNumberFormat(rate);
            }
            catch (IncorrectParametersException exception) {
                FiltersUtil.handleException(res, exception);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
