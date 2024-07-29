package org.example.ecommerce.product.service;

import org.example.ecommerce.product.dto.request.CreateProductRequest;
import org.example.ecommerce.product.dto.request.UpdateProductRequest;

public interface ProductManagementService {
    void create(CreateProductRequest createProductRequest);
    void update(String uuidProduct, UpdateProductRequest productCreateRequest);
    void delete(String uuidProduct);
}
