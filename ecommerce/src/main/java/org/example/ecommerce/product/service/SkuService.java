package org.example.ecommerce.product.service;

import org.example.ecommerce.product.dto.request.SkuRequest;

import java.util.List;
import java.util.Map;

public interface SkuService {
    Map<String, String> saveSku(String uuidProduct, Map<String, List<Map<String, SkuRequest>>> skus);
}
