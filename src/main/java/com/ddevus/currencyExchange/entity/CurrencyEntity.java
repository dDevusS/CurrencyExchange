package com.ddevus.currencyExchange.entity;

public class CurrencyEntity {

    private int id;
    private String code;
    private String name;
    private String sing;

    public CurrencyEntity(String code, String name, String sing) {
        this.code = code;
        this.name = name;
        this.sing = sing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSing() {
        return sing;
    }

    public void setSing(String sing) {
        this.sing = sing;
    }
}
