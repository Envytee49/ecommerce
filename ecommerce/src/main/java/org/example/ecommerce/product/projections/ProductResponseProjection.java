package org.example.ecommerce.product.projections;

public interface ProductResponseProjection {
    String getUuidProduct();

    String getTitle();

    double getPrice();

    double getDiscount();

    Long getSold();

    Double getAverageRating();
}
