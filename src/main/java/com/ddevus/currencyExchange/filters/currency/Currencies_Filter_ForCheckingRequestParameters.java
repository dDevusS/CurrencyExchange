package com.ddevus.currencyExchange.filters.currency;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.utils.ExceptionHandlerForFilterUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter ("/currencies")
public class Currencies_Filter_ForCheckingRequestParameters implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!((HttpServletRequest) request).getMethod().equals("POST")) {
            chain.doFilter(request, response);
        }

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sing = request.getParameter("sing");

        if (name == null || code == null || sing == null) {
            var exception = new IncorrectParametersException("Required parameters are missing."
                    , WrapperException.ErrorReason.MISSING_PARAMETERS);
            ExceptionHandlerForFilterUtil.handleException(response, exception);
        }
        else if (code.length() != 3 || sing.length() > 3) {
            var exception = new IncorrectParametersException("Required parameters are incorrect."
                    , WrapperException.ErrorReason.INCORRECT_PARAMETERS);
            ExceptionHandlerForFilterUtil.handleException(response, exception);
        }
        else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
