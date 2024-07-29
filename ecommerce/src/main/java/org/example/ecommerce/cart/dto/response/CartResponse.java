package org.example.ecommerce.cart.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class CartResponse {
    private List<CartItemResponse> cartItems;
    private int total;
}
