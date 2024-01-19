package com.ddevus.currencyExchange.filters.currency;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.ddevus.currencyExchange.utils.FiltersUtil.checkCurrencyPathCode;
import static com.ddevus.currencyExchange.utils.FiltersUtil.handleException;

@WebFilter("/currency/*")
public class CurrencyParametersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var req = ((HttpServletRequest) request);
        String servletPath = req.getPathInfo();
        String[] pathParts = servletPath.split("/");

        try {
            checkCurrencyPathCode(pathParts);
        }
        catch (IncorrectParametersException exception) {
            handleException(response, exception);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
