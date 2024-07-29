package org.example.ecommerce.common.constants;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDER_PLACED(0, "Order has been placed"),
    ORDER_APPROVED(1, "Order has been approved"),
    ORDER_DECLINED(2, "Order has been declined"),
    ORDER_SHIPPED(3, "Order has been shipped"),
    DELIVERED(4, "Order has been delivered"),
    CANCELLED(5, "Order has been cancelled"),
    PICK_UP_INITIATED(6, "Pick up has been initiated"),
    PICK_UP_COMPLETED(7, "Pick up has been completed");

    private final int activateCode;
    private final String description;

    OrderStatus(int activateCode, String description) {
        this.activateCode = activateCode;
        this.description = description;
    }
}
