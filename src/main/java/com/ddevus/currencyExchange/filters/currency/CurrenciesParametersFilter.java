package com.ddevus.currencyExchange.filters.currency;

import com.ddevus.currencyExchange.exceptions.IncorrectParametersException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.ddevus.currencyExchange.utils.FiltersUtil.checkCurrencyParameters;
import static com.ddevus.currencyExchange.utils.FiltersUtil.handleException;

@WebFilter("/currencies")
public class CurrenciesParametersFilter implements Filter {

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

            try {
                checkCurrencyParameters(name, code, sign);
            }
            catch (IncorrectParametersException exception){
                handleException(response, exception);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
