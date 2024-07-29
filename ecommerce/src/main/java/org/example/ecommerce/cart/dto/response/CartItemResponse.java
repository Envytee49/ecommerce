package org.example.ecommerce.cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.cart.model.CartItem;

@Builder
@Getter
public class CartItemResponse {
    private String uuidCartItem;

    private String uuidCart;

    private String uuidProduct;

    private String uuidShop;

    private double unitPrice;

    private double subTotal;

    private double discount;

    private int quantity;

    private int active;

    public static CartItemResponse from(CartItem cartItem) {
        return CartItemResponse.builder()
                .uuidCartItem(cartItem.getUuidCartItem())
                .uuidCart(cartItem.getUuidCart())
                .uuidProduct(cartItem.getUuidProduct())
                .unitPrice(cartItem.getUnitPrice())
                .uuidShop(cartItem.getUuidShop())
                .discount(cartItem.getDiscount())
                .subTotal(cartItem.getUnitPrice() * cartItem.getQuantity())
                .quantity(cartItem.getQuantity())
                .active(cartItem.getActive())
                .build();
    }
}
