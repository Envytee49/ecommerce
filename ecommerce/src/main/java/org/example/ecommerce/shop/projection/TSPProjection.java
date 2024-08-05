package org.example.ecommerce.shop.projection;

public interface TSPProjection {
    String getUuidProduct();
    String getProductTitle();
    double getRevenue();
    int getOrderCounts();
    long getUnitsSold();
}
