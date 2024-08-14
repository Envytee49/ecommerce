package org.example.ecommerce.product.service;

import org.example.ecommerce.product.dto.request.SkuRequest;
import org.example.ecommerce.product.helper.VariantMap;

import java.util.List;
import java.util.Map;

/**
 * Sku Product Variant Option Service
 */
public interface SPVOService {
    void saveSPVO(Map<String,VariantMap> variantMap, Map<String, String> skuMap, Map<String, List<Map<String, SkuRequest>>> skus);
}
