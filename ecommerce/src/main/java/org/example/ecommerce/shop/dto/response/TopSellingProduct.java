package org.example.ecommerce.shop.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopSellingProduct {
    private String uuidProduct;
    private String productTitle;
    private long unitsSold;
    private int orderCounts;
    private double revenue;
}
