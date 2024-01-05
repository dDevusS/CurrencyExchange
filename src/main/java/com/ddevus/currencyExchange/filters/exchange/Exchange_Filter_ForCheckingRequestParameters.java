package com.ddevus.currencyExchange.filters.exchange;

import com.ddevus.currencyExchange.utils.FiltersUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter ("/exchange")
public class Exchange_Filter_ForCheckingRequestParameters implements Filter {

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

        FiltersUtil.checkNumberFormat(amount);
        FiltersUtil.checkSentCodeParameters(baseCurrencyCode, targetCurrencyCode);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
