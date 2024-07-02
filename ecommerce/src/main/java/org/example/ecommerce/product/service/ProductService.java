package org.example.ecommerce.product.service;

import org.example.ecommerce.product.dto.request.*;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductsResponse;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    void addProduct(CreateProductRequest createProductRequest);
    void updateProduct(UpdateProductRequest productCreateRequest);
    void deleteProduct(String uuidProduct);
    ProductsResponse getAllProducts(FetchProductsRequest fetchProductsRequest);
    ProductDetailResponse getProductByUuid(String uuidProduct);
}
