package org.example.ecommerce.product.dto.request;

import lombok.Getter;

@Getter
public class SkuRequest {
    private String sku;
    private int quantity;
    private double price;
    private double discount;
}
