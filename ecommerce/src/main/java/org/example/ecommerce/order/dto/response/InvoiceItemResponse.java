package org.example.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.cart.model.CartItem;

@Getter
@Builder
public class InvoiceItemResponse {

    private String uuidProduct;

    private String uuidCartItem;

    private double unitPrice;

    private double subtotal;

    private double discount;

    private double discountPrice;

    private int quantity;

    public static InvoiceItemResponse from(CartItem cartItem) {
        return InvoiceItemResponse.builder()
                .uuidProduct(cartItem.getUuidProduct())
                .unitPrice(cartItem.getUnitPrice())
                .subtotal(cartItem.getUnitPrice() * cartItem.getQuantity())
                .discount(cartItem.getDiscount())
                .discountPrice(cartItem.getDiscount() * cartItem.getUnitPrice())
                .quantity(cartItem.getQuantity())
                .uuidCartItem(cartItem.getUuidCartItem())
                .build();
    }

}
