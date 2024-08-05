package org.example.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class InvoiceDetailResponse {
    private String shopName;
    private String uuidShop;
    private List<CartItemResponse> items;
    private double subTotal;
    private double totalAmount;
    private double discount;
    private double shipping;
}
