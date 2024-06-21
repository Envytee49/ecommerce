package org.example.ecommerce.product.service;

import org.example.ecommerce.product.dto.request.ProductCreateRequest;
import org.example.ecommerce.product.dto.request.ProductUpdateRequest;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductsResponse;
import org.example.ecommerce.product.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProductService {
    Product addProduct(ProductCreateRequest productCreateRequest);
    Product updateProduct(ProductUpdateRequest productCreateRequest, String uuidProduct);
    void deleteProduct(String uuidProduct);
    ProductsResponse getAllProducts(int page, int size);
    ProductDetailResponse getProductByUuid(String uuidProduct);
}
