package org.example.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class InvoiceDetailResponse {
    private String seller;
    private String uuidShop;
    private List<OrderItemResponse> items;
    private double subTotal;
    private double totalAmount;
    private double promotion;
    private double shipping;
}
