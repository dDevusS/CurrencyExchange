package com.ddevus.currencyExchange.filters.currency;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter ("/currency/*")
public class Currency_Filter_ForCheckingRequestParameters implements Filter {

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

        if (pathParts.length != 2 | pathParts.length > 3) {
            throw new IncorrectParametersException("Required parameters are incorrect."
                    , WrapperException.ErrorReason.INCORRECT_PARAMETERS);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
