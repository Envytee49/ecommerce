package org.example.ecommerce.order.service;

import org.example.ecommerce.cart.model.CartItem;

import java.util.List;

public interface ShippingService {
    double computeShipping(String uuidUAddress, String uuidShop, List<CartItem> cartItems);
}

