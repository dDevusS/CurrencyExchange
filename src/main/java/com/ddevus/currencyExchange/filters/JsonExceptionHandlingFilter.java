package com.ddevus.currencyExchange.filters;

import com.ddevus.currencyExchange.exceptions.BasicApplicationException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

import static com.ddevus.currencyExchange.utils.FiltersUtil.handleException;

@WebFilter("/*")
public class JsonExceptionHandlingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        }
        catch (BasicApplicationException exception) {
            handleException(response, exception);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
