package org.example.ecommerce.order.service;

import org.example.ecommerce.order.dto.request.InvoiceRequest;
import org.example.ecommerce.order.dto.request.PlaceOrderRequest;
import org.example.ecommerce.order.dto.response.InvoiceResponse;

public interface OrderService {
    void placeOrder(PlaceOrderRequest request);
    void cancelOrder(String uuidOrder);
    void declineOrder(String uuidOrder);
    void approveOrder(String uuidOrder);
}
