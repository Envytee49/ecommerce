package org.example.ecommerce.common.constants;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    COD("Cash On Delivery"),
    VNPAY("VNPAY");
    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

}
