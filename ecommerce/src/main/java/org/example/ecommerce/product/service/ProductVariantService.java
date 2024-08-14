package org.example.ecommerce.product.service;

import org.example.ecommerce.product.dto.request.ProductVariantRequest;
import org.example.ecommerce.product.dto.response.ProductVariantResponse;
import org.example.ecommerce.product.helper.VariantMap;

import java.util.List;
import java.util.Map;

public interface ProductVariantService {
    List<ProductVariantResponse> getProductVariants(String uuidProduct);
    Map<String, VariantMap> saveProductVariant(String uuidProduct, List<ProductVariantRequest> productVariantRequest);
}
