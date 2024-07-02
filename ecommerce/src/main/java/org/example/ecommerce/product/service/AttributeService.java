package org.example.ecommerce.product.service;

import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public interface AttributeService {
    Map<String, String> findByUuidProduct(String uuidProduct);
}
