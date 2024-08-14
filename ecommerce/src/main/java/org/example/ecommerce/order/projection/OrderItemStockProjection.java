package org.example.ecommerce.order.projection;

public interface OrderItemStockProjection {
    String getTitle();
    String getUuidCartItem();

    String getProductVariantOptions();
    String getUuidProduct();
    int getQuantity();
    int getStock();
}
