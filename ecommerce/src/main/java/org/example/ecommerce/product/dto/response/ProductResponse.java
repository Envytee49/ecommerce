package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.projections.ProductResponseProjection;

@Getter
@Builder
public class ProductResponse {
    private String uuidProduct;

    private String title;

    private double price;

    private double discount;

    private double priceAfterDiscount;

    private Double averageRating;

    private Integer sold;

    public static ProductResponse from(ProductResponseProjection product) {
        return ProductResponse.builder()
                .averageRating(product.getAverageRating())
                .sold(product.getSold())
                .discount(product.getDiscount())
                .priceAfterDiscount(product.getPrice() * (1 - product.getDiscount()))
                .price(product.getPrice())
                .title(product.getTitle())
                .uuidProduct(product.getUuidProduct())
                .build();
    }

}
