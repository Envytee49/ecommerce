package org.example.ecommerce.product.projections;

public interface ProductResponseProjection {
    String getUuidProduct();

    String getTitle();

    double getDiscount();

    double getPrice();

    Double getAverageRating();

    Integer getSold();
}
