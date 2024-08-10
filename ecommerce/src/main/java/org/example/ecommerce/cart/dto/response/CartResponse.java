package org.example.ecommerce.cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Builder
public class CartResponse {
    private String uuidShop;
    private String shopName;
    private List<CartItemResponse> cartItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartResponse that = (CartResponse) o;
        return Objects.equals(uuidShop, that.uuidShop);
    }

    public void updateCartItemList(CartItemResponse cartItemResponse) {
        this.cartItems.add(cartItemResponse);
    }
}
