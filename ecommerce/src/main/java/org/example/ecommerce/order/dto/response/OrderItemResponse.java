package org.example.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.cart.model.CartItem;

@Getter
@Builder
public class OrderItemResponse {

    private String uuidProduct;

    private String uuidCartItem;

    private double unitPrice;

    private double totalPrice;

    private double discountPrice;

    private int quantity;

    public static OrderItemResponse from(CartItem cartItem) {
        return OrderItemResponse.builder()
                .uuidProduct(cartItem.getUuidProduct())
                .unitPrice(cartItem.getUnitPrice())
                .totalPrice(cartItem.getUnitPrice() * cartItem.getQuantity())
                .discountPrice(cartItem.getDiscount())
                .quantity(cartItem.getQuantity())
                .uuidCartItem(cartItem.getUuidCartItem())
                .build();
    }

}
