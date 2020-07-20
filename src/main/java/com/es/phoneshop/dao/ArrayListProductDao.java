package com.es.phoneshop.dao;

import com.es.phoneshop.enums.ProductOrderBy;
import com.es.phoneshop.enums.ProductSortBy;
import com.es.phoneshop.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractDefaultDao<Product> implements ProductDao {
    private ArrayList<Product> products;


    private ArrayListProductDao() {
        this.products = new ArrayList<>();
        super.init(this.products);

    }

    public static ArrayListProductDao getInstance() {
        return ProductDaoHolder.instance;
    }

    private static class ProductDaoHolder {
        private static final ArrayListProductDao instance = new ArrayListProductDao();
    }


    private List<Product> findProductsByQuery(String query, List<Product> products) {
        String[] words = query.toLowerCase().split(" ");
        return products.stream().filter(product -> Arrays.stream(words)
                .anyMatch(word -> product.getDescription().toLowerCase().contains(word)))
                .sorted(Comparator.comparingInt(product -> (int) Arrays.stream(words)
                        .filter(word -> product.getDescription().toLowerCase().contains(word)).count()))
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Product> findProducts(String query, String order, String sort) {
        List<Product> foundProducts = this.products.stream()
                .filter(product -> product.getStock() > 0)
                .filter(product -> product.getCurrentPrice() != null)
                .collect(Collectors.toList());
        if (query != null) {
            foundProducts = findProductsByQuery(query, foundProducts);
        }
        if (order != null) {
            foundProducts = sortProductsByOrderAndSort(foundProducts, order, sort);
        }
        return foundProducts;
    }

    @Override
    public void reduceAmountProducts(Product orderProduct, Long val) {
        Product currentProduct = getById(orderProduct.getId());
        currentProduct.setStock(currentProduct.getStock() - val.intValue());
    }

    private List<Product> sortProductsByOrderAndSort(List<Product> products, String order, String sort) {

        Comparator<Product> productComparator = null;
        switch (ProductOrderBy.valueOf(order.toUpperCase())) {
            case PRICE:
                productComparator = Comparator.comparing(o -> o.getCurrentPrice().getCost());
                break;
            case DESCRIPTION:
                productComparator = Comparator.comparing(Product::getDescription);
                break;
        }

        if (ProductSortBy.valueOf(sort.toUpperCase()) == ProductSortBy.DESC) {
            productComparator = productComparator.reversed();
        }
        return products.stream().sorted(productComparator).collect(Collectors.toList());
    }

    @Override
    public Product getById(Long id) {
        return super.getById(id);
    }

    @Override
    public void save(Product product) {
        try {
            Product existProduct = getById(product.getId());
            update(product, existProduct);
        } catch (NoSuchElementException | IllegalArgumentException illegalArgumentException) {
            product.setId(Integer.toUnsignedLong(products.size()));
            products.add(product);
        }
    }

    private void update(Product updateProduct, Product oldProduct) {
        oldProduct.setCode(updateProduct.getCode());
        oldProduct.setCurrency(updateProduct.getCurrency());
        oldProduct.setDescription(updateProduct.getDescription());
        oldProduct.setImageUrl(updateProduct.getImageUrl());
        oldProduct.setPrices(updateProduct.getPrices());
        oldProduct.setStock(updateProduct.getStock());
    }

    @Override
    public synchronized void deleteById(Long id) {
        products.removeIf(product -> product.getId().equals(id));
    }

    @Override
    public void saveAll(List<Product> newProducts) {
        newProducts.forEach(this::save);
    }
}