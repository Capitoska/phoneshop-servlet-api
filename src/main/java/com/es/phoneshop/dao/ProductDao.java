package com.es.phoneshop.dao;

import com.es.phoneshop.model.AdvancedSearch;
import com.es.phoneshop.model.Product;

import java.util.List;

public interface ProductDao extends DefaultDao<Product> {
    List<Product> findProducts(String query, String order, String sort);

    List<Product> findProductsByAdvancedSearch(AdvancedSearch advancedSearch);

    void reduceAmountProducts(Product product, Long val);
}
