package org.example.ecommerce.order.projection;

import org.example.ecommerce.cart.model.CartItem;
public interface InvoiceItemProjection {
    CartItem getCartItem();

    String getUuidShop();
}
