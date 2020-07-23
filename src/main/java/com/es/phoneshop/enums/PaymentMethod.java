package com.es.phoneshop.enums;

import java.util.Arrays;

public enum PaymentMethod {
    MONEY("money", 0),
    CREDIT_CART("Credit cart", 1);

    private String name;
    private Integer id;

    PaymentMethod(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public static PaymentMethod valueOf(Integer id) {
        return Arrays.stream(PaymentMethod.values())
                .filter(paymentMethod -> paymentMethod.id.equals(id))
                .findFirst().orElseThrow(NoSuchFieldError::new);
    }

}
