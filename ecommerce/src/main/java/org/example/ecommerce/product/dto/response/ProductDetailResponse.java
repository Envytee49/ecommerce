package org.example.ecommerce.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.product.model.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
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


    private List<ProductVariantResponse> productVariants;

    private List<CategoryResponse> categories;

    public static ProductDetailResponse from(Product product,
                                             List<ProductVariantResponse> productVariants,
                                             List<CategoryResponse> categories) {
        return ProductDetailResponse.builder()
                .uuidProduct(product.getUuidProduct())
                .publishedDate(product.getPublishedDate())
                .price(product.getPrice())
                .title(product.getTitle())
                .metaTitle(product.getMetaTitle())
                .quantity(product.getQuantity())
                .summary(product.getSummary())
                .description(product.getDescription())
                .productVariants(productVariants)
                .categories(categories)
                .build();
    }

}
