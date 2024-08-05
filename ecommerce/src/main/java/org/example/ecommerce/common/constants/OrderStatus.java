package org.example.ecommerce.common.constants;

import lombok.Getter;

import java.util.List;

@Getter
public enum OrderStatus {
    ORDER_PLACED,
    ORDER_APPROVED,
    ORDER_DECLINED,
    ORDER_SHIPPED,
    DELIVERED,
    CANCELLED;

}
