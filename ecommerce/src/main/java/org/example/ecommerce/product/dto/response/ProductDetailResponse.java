package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.product.model.Product;

import java.time.LocalDateTime;
@Builder
@Getter
public class ProductDetailResponse {
    private String uuidProduct;

    private String title;

    private String metaTitle;

    private String summary;

    private int type;

    private double price;

    private int quantity;

    private String description;

    private LocalDateTime publishedDate;

    public static ProductDetailResponse from(Product product) {
        return ProductDetailResponse.builder()
                .uuidProduct(product.getUuidProduct())
                .publishedDate(product.getPublishedDate())
                .price(product.getPrice())
                .title(product.getTitle())
                .metaTitle(product.getMetaTitle())
                .quantity(product.getQuantity())
                .summary(product.getSummary())
                .description(product.getDescription())
                .type(product.getType())
                .build();
    }
}
