package com.ddevus.currencyExchange.filters.currency;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import com.ddevus.currencyExchange.utils.FiltersUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/currencies")
public class Currencies_Filter_ForCheckingRequestParameters implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest) request).getMethod().equals("POST")) {
            String name = request.getParameter("name");
            String code = request.getParameter("code");
            String sign = request.getParameter("sign");

            if (name == null || code == null || sign == null) {
                var exception = new IncorrectParametersException("Required parameters are missing.");
                FiltersUtil.handleException(response, exception);
            }
            else if (code.length() != 3 || sign.length() > 3) {
                var exception = new IncorrectParametersException("Required parameters are incorrect.");
                FiltersUtil.handleException(response, exception);
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
