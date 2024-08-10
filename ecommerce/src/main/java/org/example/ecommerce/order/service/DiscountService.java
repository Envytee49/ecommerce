package org.example.ecommerce.order.service;

import org.example.ecommerce.cart.model.CartItem;

import java.util.List;
import java.util.Map;

public interface DiscountService {
    double computeVoucherDiscount(double subTotal,
                                  Map<String, String> shopVouchers,
                                  String uuidShop,
                                  String discountCashbackVoucher,
                                  List<CartItem> cartItems);

    double computeShippingDiscount(String freeShippingVoucher,
                                   String uuidUAddress,
                                   String uuidShop,
                                   double shippingSubtotal,
                                   List<CartItem> cartItems);
}
