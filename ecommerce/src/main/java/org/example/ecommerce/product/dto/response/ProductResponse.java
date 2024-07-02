package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.product.model.Product;

@Getter
@Builder
public class ProductResponse {
    private String uuidProduct;

    private String title;

    private double price;

    private double averageRating;

    private int sold;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .averageRating(3.0)
                .sold(1000)
                .price(product.getPrice())
                .title(product.getTitle())
                .uuidProduct(product.getUuidProduct())
                .build();
    }

}
