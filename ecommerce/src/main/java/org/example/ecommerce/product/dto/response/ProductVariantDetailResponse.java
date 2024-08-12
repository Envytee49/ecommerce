package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.cart.dto.request.ProductVariantRequest;

import java.util.Set;

@Getter
@Builder
public class ProductVariantDetailResponse {
    private String uuidProduct;
    private Set<ProductVariantRequest> productVariants;
    private int stock;
    private double price;
}
