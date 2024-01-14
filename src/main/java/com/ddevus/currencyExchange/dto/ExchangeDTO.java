package com.ddevus.currencyExchange.dto;

import com.ddevus.currencyExchange.entity.ExchangeRate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
public class ExchangeDTO {

    ExchangeRate exchangeRate;
    BigDecimal amount;
    BigDecimal convertedAmount;

}
