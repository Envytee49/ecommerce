package org.example.ecommerce.order.projection;

public interface OrderItemProjection {
    String getUuidOrderItem();
    String getUuidOrder();
    String getUuidShop();
    String getShopName();
    String getUuidProduct();
    String getUuidSku();
    String getProductTitle();
    String getProductVariantOption();
    int getQuantity();
    double getOriginalPrice();
    double getDiscountPercentage();
    double getMerchandiseSubtotal();
    double getShippingSubtotal();
    Double getShippingDiscountSubtotal();
    Double getVoucherDiscount();
    String getUuidUAddress();
    double getTotalPayment();
}
