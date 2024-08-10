package org.example.ecommerce.product.service;

import org.example.ecommerce.product.dto.response.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {
    List<ProductVariantResponse> getProductVariants(String uuidProduct);
}
