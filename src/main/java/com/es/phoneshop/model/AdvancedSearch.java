package com.es.phoneshop.model;

import java.math.BigDecimal;

public class AdvancedSearch {
    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;


    public AdvancedSearch(String description, BigDecimal minPrice, BigDecimal maxPrice, Integer minStock) {
        this.description = description;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minStock = minStock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }
}
