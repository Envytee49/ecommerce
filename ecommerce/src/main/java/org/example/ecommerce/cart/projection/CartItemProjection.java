package org.example.ecommerce.cart.projection;

public interface CartItemProjection {
    String getTitle();

    String getUuidCartItem();

    String getUuidProduct();

    String getUuidSku();

    String getProductVariantOptions();

    double getUnitPrice();

    double getDiscount();

    int getQuantity();

    int getActive();

    String getUuidShop();

    String getShopName();
}
