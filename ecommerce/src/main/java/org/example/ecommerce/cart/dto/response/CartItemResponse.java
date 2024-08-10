package org.example.ecommerce.cart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.cart.projection.CartItemProjection;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemResponse {
    private String title;

    private String uuidCartItem;

    private String productVariantOptions;

    private String uuidProduct;

    private String uuidSku;

    private double unitPrice;

    private double subTotal;

    private double discountPercentage;

    private double discountPrice;

    private int quantity;

    private int active;

    public static CartItemResponse from(CartItemProjection cartItem) {
        return CartItemResponse.builder()
                .title(cartItem.getTitle())
                .uuidCartItem(cartItem.getUuidCartItem())
                .uuidSku(cartItem.getUuidSku())
                .uuidProduct(cartItem.getUuidProduct())
                .unitPrice(cartItem.getUnitPrice())
                .discountPercentage(cartItem.getDiscount() * 100)
                .discountPrice(cartItem.getDiscount() * cartItem.getUnitPrice())
                .productVariantOptions(cartItem.getProductVariantOptions())
                .subTotal(cartItem.getUnitPrice() * cartItem.getQuantity())
                .quantity(cartItem.getQuantity())
                .active(cartItem.getActive())
                .build();
    }
}
