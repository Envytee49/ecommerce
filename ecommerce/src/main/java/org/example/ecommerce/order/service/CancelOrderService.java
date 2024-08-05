package org.example.ecommerce.order.service;

import org.example.ecommerce.order.dto.request.CancelOrderRequest;
import org.example.ecommerce.order.model.CancelledOrderReason;

import java.util.List;

public interface CancelOrderService {
    void cancelOrder(CancelOrderRequest request);
    List<CancelledOrderReason> getCancelledOrderReasons();
    Object getCancelledOrderDetail(String uuidOrder);
}
