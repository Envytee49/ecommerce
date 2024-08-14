package org.example.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {
    private String uuidOrderItem;
    private String uuidProduct;
    private String uuidSku;
    private String title;
    private String variant;
    private double price;
    private double discount;
    private double quantity;
}
