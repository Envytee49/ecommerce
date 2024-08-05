package org.example.ecommerce.cart.projection;

import org.example.ecommerce.cart.model.CartItem;

public interface CartItemProjection {
    CartItem getCartItem();
    String getUuidShop();
}
