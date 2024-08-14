package org.example.ecommerce.product.dto.request;

import lombok.Getter;

import java.util.List;
@Getter

public class ProductVariantRequest {
    private String variantName;
    private List<String> productVariantOptions;
}
