package org.example.ecommerce.order.service;

public interface OrderManagementService {
    void declineOrder(String uuidOrder);
    void approveOrder(String uuidOrder);

}
