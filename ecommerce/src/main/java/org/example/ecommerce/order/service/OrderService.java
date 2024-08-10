package org.example.ecommerce.order.service;

import org.example.ecommerce.order.dto.request.FetchOrderDetailRequest;
import org.example.ecommerce.order.dto.request.FetchOrderRequest;
import org.example.ecommerce.order.dto.request.PlaceOrderRequest;
import org.example.ecommerce.order.dto.response.OrderHistoryResponse;
import org.example.ecommerce.order.dto.response.OrderItemDetailResponse;

public interface OrderService {
    void placeOrder(PlaceOrderRequest request);
    OrderHistoryResponse getOrderHistory(FetchOrderRequest request);
    OrderItemDetailResponse getOrderDetails(FetchOrderDetailRequest request);

    void changeAddress(String uuidUAddress);
}
