package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.product.projections.ProductResponseProjection;

@Getter
public class ProductResponse {
    private String uuidProduct;

    private String title;

    private double price;

    private double discount;

    private double priceAfterDiscount;

    private Double averageRating;

    private Double sold;

    public ProductResponse(String uuidProduct,
                           String title,
                           Double price,
                           Double discount,
                           Double averageRating,
                           Double sold) {
        this.uuidProduct = uuidProduct;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.priceAfterDiscount = price * (1-discount);
        this.averageRating = averageRating;
        this.sold = sold;
    }
}
