package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.order.service.ShippingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingServiceImpl implements ShippingService {
    @Override
    public double computeShipping(String uuidUAddress, String uuidShop, List<CartItem> cartItems) {
        log.info("Compute shipping");
        return 20;
    }
}
