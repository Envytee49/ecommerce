package org.example.ecommerce.shop.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TopOrderedProduct {
    private String uuidProduct;
    private String productName;
    private double revenue;
    private int orderCounts;
}
