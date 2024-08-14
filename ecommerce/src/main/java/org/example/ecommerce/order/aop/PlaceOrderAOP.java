package org.example.ecommerce.order.aop;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.projection.OrderItemStockProjection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceOrderAOP {
    private final CartRepository cartRepository;

    public void checkProductStock(List<String> orderingItems) {
        List<OrderItemStockProjection> orderingItemProjections = cartRepository.findOrderingItemStock(orderingItems);
        for (OrderItemStockProjection orderingItem : orderingItemProjections) {
            if(orderingItem.getStock() < orderingItem.getQuantity())
                throw new AppException(ErrorCode.NOT_ENOUGH_STOCK
                        .setMessage("NOT ENOUGH STOCK FOR" +
                                orderingItem.getTitle() + ":" +
                                orderingItem.getProductVariantOptions()));
        }
    }
}
