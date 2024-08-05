package org.example.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.order.model.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Getter
@Setter
@Builder
public class ShopOrder {
    private String uuidOrder;
    private String uuidShop;
    private String shopName;
    private Double shippingFee;
    private Double totalPrice;
    private List<OrderItem> orderItems;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopOrder shopOrder = (ShopOrder) o;
        return Objects.equals(uuidOrder, shopOrder.uuidOrder) && Objects.equals(uuidShop, shopOrder.uuidShop);
    }

    public void updateOrderItemList(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }
}
