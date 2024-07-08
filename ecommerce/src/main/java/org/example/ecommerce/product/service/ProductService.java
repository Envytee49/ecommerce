package org.example.ecommerce.product.service;

import org.example.ecommerce.product.dto.request.*;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductsResponse;


public interface ProductService {
    void create(CreateProductRequest createProductRequest);
    void update(String uuidProduct, UpdateProductRequest productCreateRequest);
    void delete(String uuidProduct);
    ProductsResponse getAll(int page, int size);
    ProductDetailResponse getByUuid(String uuidProduct);
}
