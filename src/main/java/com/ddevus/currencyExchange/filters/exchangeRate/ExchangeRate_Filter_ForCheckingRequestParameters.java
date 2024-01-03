package com.ddevus.currencyExchange.filters.exchangeRate;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.utils.ExceptionHandlerForFilterUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter ("/exchangeRate/*")
public class ExchangeRate_Filter_ForCheckingRequestParameters implements Filter {

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

        if (("GET").equals(req.getMethod())) {
            if (ExceptionHandlerForFilterUtil.isCorrectParameter(pathInfo)) {
                chain.doFilter(request, response);
            }
            else {
                var exception = new IncorrectParametersException("Required parameters are incorrect."
                        , WrapperException.ErrorReason.INCORRECT_PARAMETERS);

                ExceptionHandlerForFilterUtil.handleException(res, exception);
            }
        }

        if (("PATCH").equals(req.getMethod())) {
            if (!ExceptionHandlerForFilterUtil.isCorrectParameter(pathInfo)) {
                var exception
                        = new IncorrectParametersException("Required parameters are incorrect."
                        , WrapperException.ErrorReason.INCORRECT_PARAMETERS);

                ExceptionHandlerForFilterUtil.handleException(res, exception);
            }
            else if (req.getParameter("rate") == null) {
                var exception
                        = new IncorrectParametersException("Required parameter is missed."
                        , WrapperException.ErrorReason.INCORRECT_PARAMETERS);

                ExceptionHandlerForFilterUtil.handleException(res, exception);
            }
            else {
                chain.doFilter(request, response);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
