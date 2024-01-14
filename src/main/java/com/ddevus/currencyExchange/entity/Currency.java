package com.ddevus.currencyExchange.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Currency {

    private int id;
    private String code;
    private String name;
    private String sign;

    public Currency(String name, String code, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

}
