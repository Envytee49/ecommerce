package org.example.ecommerce.order.projection;

public interface OrderItemProjection {
    String getUuidOrder();
    String getUuidShop();
    String getShopName();
    String getUuidProduct();
    String getUuidUAddress();
    int getQuantity();
    double getOriginalPrice();
    double getDiscountPrice();
    double getShippingFee();
    double getTotalPrice();
}
